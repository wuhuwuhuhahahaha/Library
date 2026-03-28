package cn.whpu.library.service;

import cn.whpu.library.entity.Book;
import cn.whpu.library.entity.Borrow;
import cn.whpu.library.entity.User;
import cn.whpu.library.mapper.BookMapper;
import cn.whpu.library.mapper.BorrowMapper;
import cn.whpu.library.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private BookMapper bookMapper;
    
    @Autowired
    private BorrowMapper borrowMapper;
    
    // 获取总用户数
    public Integer getTotalUsers() {
        List<User> users = userMapper.findAll();
        return users.size();
    }
    
    // 获取总图书数
    public Integer getTotalBooks() {
        List<Book> books = bookMapper.findAll();
        return books.size();
    }
    
    // 获取总借阅数
    public Integer getTotalBorrows() {
        List<Borrow> borrows = borrowMapper.findAll();
        return borrows.size();
    }
    
    // 获取月度借阅趋势数据（最近 12 个月）
    public List<Map<String, Object>> getMonthlyTrend() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 这里简化处理，返回模拟数据
        // 实际项目中应该从数据库按月份统计借阅记录
        String[] months = {"1 月", "2 月", "3 月", "4 月", "5 月", "6 月", 
                          "7 月", "8 月", "9 月", "10 月", "11 月", "12 月"};
        Random random = new Random();
        
        for (String month : months) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", month);
            item.put("count", random.nextInt(200) + 100);
            result.add(item);
        }
        
        return result;
    }
    
    // 获取图书分类统计数据
    public List<Map<String, Object>> getBookCategoryStats() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 这里简化处理，返回模拟数据
        // 实际项目中需要根据图书分类表进行统计
        Map<String, Integer> categories = new HashMap<>();
        categories.put("计算机", 400);
        categories.put("文学", 300);
        categories.put("历史", 200);
        categories.put("科学", 150);
        categories.put("艺术", 100);
        
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("category", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        
        return result;
    }
}
