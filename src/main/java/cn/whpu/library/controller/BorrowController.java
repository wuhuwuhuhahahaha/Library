package cn.whpu.library.controller;

import cn.whpu.library.dto.ApiResponse;
import cn.whpu.library.entity.Borrow;
import cn.whpu.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BorrowController {
    
    @Autowired
    private BorrowService borrowService;
    
    // 获取借阅列表（支持分页和条件查询）
    @GetMapping("/borrow")
    public ApiResponse<Map<String, Object>> getBorrows(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String bookName,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Map<String, Object> result = borrowService.getBorrows(username, bookName, pageNum, pageSize);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 按用户名查询借阅记录
    @GetMapping("/borrow/user/{username}")
    public ApiResponse<List<Borrow>> getBorrowsByUsername(@PathVariable String username) {
        try {
            Map<String, Object> result = borrowService.getBorrows(username, null, 1, 1000);
            List<Borrow> borrows = (List<Borrow>) result.get("list");
            return ApiResponse.success(borrows);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 按书名查询借阅记录
    @GetMapping("/borrow/book/{bookName}")
    public ApiResponse<List<Borrow>> getBorrowsByBookName(@PathVariable String bookName) {
        try {
            Map<String, Object> result = borrowService.getBorrows(null, bookName, 1, 1000);
            List<Borrow> borrows = (List<Borrow>) result.get("list");
            return ApiResponse.success(borrows);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 删除借阅记录
    @DeleteMapping("/borrow/{id}")
    public ApiResponse<Void> deleteBorrow(@PathVariable Long id) {
        try {
            if (borrowService.deleteBorrow(id)) {
                return ApiResponse.success(null);
            } else {
                return ApiResponse.error("删除失败");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
