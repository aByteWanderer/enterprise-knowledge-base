package com.knowledge.base.controller;

import com.knowledge.base.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调试测试控制器
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/db")
    public Result<Map<String, Object>> testDb() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 查询所有用户（包括已删除的）
            List<Map<String, Object>> allUsers = jdbcTemplate.queryForList(
                "SELECT id, username, email, status, deleted FROM tb_users"
            );
            result.put("allUsers", allUsers);
            result.put("count", allUsers.size());
            
            return Result.success(result);
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return Result.error(500, "查询失败: " + e.getMessage());
        }
    }
}
