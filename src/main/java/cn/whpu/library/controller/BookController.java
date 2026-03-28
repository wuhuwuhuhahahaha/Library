package cn.whpu.library.controller;

import cn.whpu.library.dto.ApiResponse;
import cn.whpu.library.dto.BorrowRequest;
import cn.whpu.library.entity.Book;
import cn.whpu.library.service.BookService;
import cn.whpu.library.service.BorrowService;
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
    public ApiResponse<Void> borrowBook(@RequestBody BorrowRequest request) {
        try {
            // 先检查图书是否存在且库存充足
            Book book = bookService.getBookByName(request.getBookName());
            if (book == null || book.getStock() <= 0) {
                return ApiResponse.error("借书失败，库存不足或图书不存在");
            }
            
            // 减少库存
            if (!bookService.borrowBook(request.getBookName())) {
                return ApiResponse.error("借书失败");
            }
            
            // 创建借阅记录
            if (!borrowService.createBorrow(request.getUserName(), request.getBookName())) {
                return ApiResponse.error("创建借阅记录失败");
            }
            
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
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
