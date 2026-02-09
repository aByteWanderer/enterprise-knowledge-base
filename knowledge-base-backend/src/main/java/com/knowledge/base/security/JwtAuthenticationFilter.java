package com.knowledge.base.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;
    
    private final UserDetailsService userDetailsService;
    
    private final JwtProperties jwtProperties;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        log.debug("[JWT Filter] Processing request: {}", requestURI);
        
        try {
            String token = getTokenFromRequest(request);
            log.debug("[JWT Filter] Token extracted: {}", StringUtils.hasText(token) ? "present" : "null/empty");
            
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token);
                log.debug("[JWT Filter] Token valid, username from token: {}", username);
                
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    log.debug("[JWT Filter] UserDetails loaded: {}, authorities: {}", 
                            userDetails.getUsername(), userDetails.getAuthorities());
                    
                    if (jwtTokenProvider.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = 
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("[JWT Filter] Authentication successful for user: {}, URI: {}", 
                                username, requestURI);
                    } else {
                        log.warn("[JWT Filter] Token validation failed for user: {}", username);
                    }
                } catch (UsernameNotFoundException e) {
                    log.warn("[JWT Filter] User not found: {}", username);
                    SecurityContextHolder.clearContext();
                }
            } else if (StringUtils.hasText(token)) {
                log.warn("[JWT Filter] Token validation failed");
            }
        } catch (Exception e) {
            log.error("[JWT Filter] Authentication error: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("[JWT Filter] Final authentication: {}, principal: {}", 
                auth != null ? auth.getClass().getSimpleName() : "null",
                auth != null ? auth.getPrincipal() : "null");
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * 从请求中获取token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getHeader());
        return jwtTokenProvider.getTokenFromHeader(bearerToken);
    }
    
}
