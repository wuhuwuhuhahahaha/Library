package cn.whpu.library.controller;

import cn.whpu.library.dto.ApiResponse;
import cn.whpu.library.dto.BorrowRequest;
import cn.whpu.library.entity.Book;
import cn.whpu.library.service.BookService;
import cn.whpu.library.service.BorrowService;
import cn.whpu.library.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @GetMapping("/book")
    public ApiResponse<Map<String, Object>> getBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Map<String, Object> result = bookService.getBooks(keyword, pageNum, pageSize);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/book/{id}")
    public ApiResponse<Book> getBook(@PathVariable Long id) {
        try {
            Book book = bookService.getBookById(id);
            if (book == null) {
                return ApiResponse.error("图书不存在");
            }
            return ApiResponse.success(book);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/book")
    public ApiResponse<Void> createBook(@RequestBody Book book) {
        try {
            if (bookService.createBook(book)) {
                return ApiResponse.success(null);
            } else {
                return ApiResponse.error("创建失败");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/book")
    public ApiResponse<Void> updateBook(@RequestBody Book book) {
        try {
            if (bookService.updateBook(book)) {
                return ApiResponse.success(null);
            } else {
                return ApiResponse.error("更新失败");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/book/{id}")
    public ApiResponse<Void> deleteBook(@PathVariable Long id) {
        try {
            if (bookService.deleteBook(id)) {
                return ApiResponse.success(null);
            } else {
                return ApiResponse.error("删除失败");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/borrow")
    public ApiResponse<Void> borrowBook(
            @RequestBody BorrowRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            // 从 token 中获取用户信息
            String userName = extractUsernameFromToken(authorization);
            if (userName == null) {
                return ApiResponse.error("未登录或 token 已过期");
            }
            
            // 执行借书操作（包含事务）
            borrowService.borrowBookTransaction(userName, request.getBookName());
            
            return ApiResponse.success(null);
        } catch (RuntimeException e) {
            // 业务异常，直接返回错误信息
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            // 其他异常，事务会自动回滚
            e.printStackTrace();
            return ApiResponse.error("借书失败：" + e.getMessage());
        }
    }
    
    // 从 Authorization header 中解析 token 获取用户名
    private String extractUsernameFromToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        
        String token = authorization.substring(7);
        
        try {
            return jwtUtils.extractUsername(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @PutMapping("/borrow/return")
    public ApiResponse<Void> returnBook(
            @RequestParam String userName,
            @RequestParam String bookName) {
        try {
            if (bookService.returnBook(bookName)) {
                return ApiResponse.success(null);
            } else {
                return ApiResponse.error("还书失败");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
