package com.knowledge.base.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {
    
    private JwtTokenProvider jwtTokenProvider;
    
    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secret", "testSecretKeyForTestingPurposesOnly!!");
        ReflectionTestUtils.setField(jwtTokenProvider, "expiration", 86400000L);
        ReflectionTestUtils.setField(jwtTokenProvider, "header", "Authorization");
        ReflectionTestUtils.setField(jwtTokenProvider, "prefix", "Bearer");
        jwtTokenProvider.init();
    }
    
    @Test
    void testGenerateToken() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        
        String token = jwtTokenProvider.generateToken(userDetails);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
    
    @Test
    void testGetUsernameFromToken() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        
        String token = jwtTokenProvider.generateToken(userDetails);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        
        assertEquals("testuser", username);
    }
    
    @Test
    void testValidateToken() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        
        String token = jwtTokenProvider.generateToken(userDetails);
        boolean isValid = jwtTokenProvider.validateToken(token, userDetails);
        
        assertTrue(isValid);
    }
    
    @Test
    void testIsTokenExpired() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        
        String token = jwtTokenProvider.generateToken(userDetails);
        boolean isExpired = jwtTokenProvider.isTokenExpired(token);
        
        assertFalse(isExpired);
    }
    
    @Test
    void testGetTokenFromHeader() {
        String token = "testToken123";
        String header = "Bearer " + token;
        
        String result = jwtTokenProvider.getTokenFromHeader(header);
        
        assertEquals(token, result);
    }
    
    @Test
    void testGetTokenFromHeaderWithNull() {
        String result = jwtTokenProvider.getTokenFromHeader(null);
        
        assertNull(result);
    }
    
    @Test
    void testGetRemainingTime() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        
        String token = jwtTokenProvider.generateToken(userDetails);
        Long remainingTime = jwtTokenProvider.getRemainingTime(token);
        
        assertNotNull(remainingTime);
        assertTrue(remainingTime > 0);
    }
    
}
