package cn.whpu.library.controller;

import cn.whpu.library.dto.ApiResponse;
import cn.whpu.library.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    // 获取仪表盘统计数据
    @GetMapping("/dashboard/stats")
    public ApiResponse<Map<String, Object>> getDashboardStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", dashboardService.getTotalUsers());
            stats.put("totalBooks", dashboardService.getTotalBooks());
            stats.put("totalBorrows", dashboardService.getTotalBorrows());
            
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 获取月度借阅趋势数据
    @GetMapping("/dashboard/monthly-trend")
    public ApiResponse<List<Map<String, Object>>> getMonthlyTrend() {
        try {
            List<Map<String, Object>> trendData = dashboardService.getMonthlyTrend();
            return ApiResponse.success(trendData);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 获取图书分类统计数据
    @GetMapping("/dashboard/book-category-stats")
    public ApiResponse<List<Map<String, Object>>> getBookCategoryStats() {
        try {
            List<Map<String, Object>> categoryData = dashboardService.getBookCategoryStats();
            return ApiResponse.success(categoryData);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
