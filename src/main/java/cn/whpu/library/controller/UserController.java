package cn.whpu.library.controller;

import cn.whpu.library.dto.ApiResponse;
import cn.whpu.library.dto.RegisterRequest;
import cn.whpu.library.dto.UserUpdateRequest;
import cn.whpu.library.entity.User;
import cn.whpu.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // 获取所有用户（简单列表，用于前端展示）
    @GetMapping("/users/simple")
    public ApiResponse<List<User>> getAllUsers() {
        try {
            List<User> users = userService.findAll();
            return ApiResponse.success(users);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 搜索用户（支持 keyword 模糊查询）
    @GetMapping("/users")
    public ApiResponse<List<User>> searchUsers(
            @RequestParam(required = false) String keyword) {
        try {
            List<User> users;
            if (keyword != null && !keyword.trim().isEmpty()) {
                users = userService.findByKeyword(keyword);
            } else {
                users = userService.findAll();
            }
            return ApiResponse.success(users);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 更新用户信息
    @PutMapping("/user")
    public ApiResponse<Void> updateUser(@RequestBody UserUpdateRequest request) {
        try {
            User user = new User();
            user.setId(request.getId());
            user.setUsername(request.getUsername());
            
            // 如果提供了新密码则更新密码
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(request.getPassword());
            }
            
            if (userService.update(user)) {
                return ApiResponse.success(null);
            } else {
                return ApiResponse.error("更新失败");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 删除用户
    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        try {
            if (userService.deleteById(id)) {
                return ApiResponse.success(null);
            } else {
                return ApiResponse.error("删除失败");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
