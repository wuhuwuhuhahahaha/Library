package cn.whpu.library.service;

import cn.whpu.library.entity.User;
import cn.whpu.library.mapper.UserMapper;
import cn.whpu.library.utils.JwtUtils;
import cn.hutool.crypto.SecureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private RedisCacheService redisCache;
    
    public User register(String username, String password) {
        User existingUser = userMapper.findByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(SecureUtil.md5(password));
        
        userMapper.insert(user);
        return user;
    }
    
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        String encryptedPassword = SecureUtil.md5(password);
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new RuntimeException("Invalid password");
        }
        
        return user;
    }
    
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("name", user.getUsername());
        
        return jwtUtils.generateToken(user.getUsername(), claims);
    }
    
    public List<User> findAll() {
        String cacheKey = "user:all";
        
        // 先从 Redis 获取缓存
        @SuppressWarnings("unchecked")
        List<User> cachedUsers = (List<User>) redisCache.get(cacheKey);
        if (cachedUsers != null) {
            return cachedUsers;
        }
        
        // 缓存未命中，从数据库查询并写入缓存
        List<User> users = userMapper.findAll();
        redisCache.set(cacheKey, users, 300); // 缓存 5 分钟
        return users;
    }
    
    public List<User> findByKeyword(String keyword) {
        String cacheKey = "user:keyword:" + keyword;
        
        // 先从 Redis 获取缓存
        @SuppressWarnings("unchecked")
        List<User> cachedUsers = (List<User>) redisCache.get(cacheKey);
        if (cachedUsers != null) {
            return cachedUsers;
        }
        
        // 缓存未命中，从数据库查询并写入缓存
        List<User> users = userMapper.findByKeyword(keyword);
        redisCache.set(cacheKey, users, 300); // 缓存 5 分钟
        return users;
    }
    
    public boolean update(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            // 不更新密码，只更新用户名
            boolean result = userMapper.updateUsername(user) > 0;
            if (result) {
                // 清除相关缓存
                redisCache.delete("user:all");
                redisCache.delete("user:id:" + user.getId());
            }
            return result;
        } else {
            // 加密密码
            user.setPassword(SecureUtil.md5(user.getPassword()));
            boolean result = userMapper.update(user) > 0;
            if (result) {
                // 清除相关缓存
                redisCache.delete("user:all");
                redisCache.delete("user:id:" + user.getId());
            }
            return result;
        }
    }
    
    public boolean deleteById(Long id) {
        boolean result = userMapper.deleteById(id) > 0;
        if (result) {
            // 清除相关缓存
            redisCache.delete("user:all");
            redisCache.delete("user:id:" + id);
        }
        return result;
    }
}
