package cn.whpu.library.controller;

import cn.whpu.library.dto.ApiResponse;
import cn.whpu.library.dto.LoginRequest;
import cn.whpu.library.dto.RegisterRequest;
import cn.whpu.library.entity.User;
import cn.whpu.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            String token = userService.generateToken(user);
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("username", user.getUsername());
            
            return ApiResponse.success(data);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/success")
    public ApiResponse<Void> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request.getUsername(), request.getPassword());
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
