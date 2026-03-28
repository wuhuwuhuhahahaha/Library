package cn.whpu.library.service;

import cn.whpu.library.entity.User;
import cn.whpu.library.mapper.UserMapper;
import cn.hutool.crypto.SecureUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    private static final String JWT_SECRET = "library-secret-key-2026";
    
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
        claims.put("username", user.getUsername());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }
}
