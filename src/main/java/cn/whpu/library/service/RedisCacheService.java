package cn.whpu.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 从 Redis 获取缓存
     * @param key 缓存键
     * @return 缓存值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 将数据写入 Redis
     * @param key 缓存键
     * @param value 缓存值
     * @param timeout 过期时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }
    
    /**
     * 删除缓存
     * @param key 缓存键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    /**
     * 检查缓存是否存在
     * @param key 缓存键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
