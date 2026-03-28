package cn.whpu.library.service;

import cn.whpu.library.entity.Book;
import cn.whpu.library.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    
    @Autowired
    private BookMapper bookMapper;
    
    public Map<String, Object> getBooks(String keyword, int pageNum, int pageSize) {
        List<Book> allBooks;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            allBooks = bookMapper.findByKeyword(keyword);
        } else {
            allBooks = bookMapper.findAll();
        }
        
        // 手动分页
        int total = allBooks.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        
        List<Book> pageData;
        if (fromIndex >= total) {
            pageData = List.of();
        } else {
            pageData = allBooks.subList(fromIndex, toIndex);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageData);
        result.put("total", total);
        
        return result;
    }
    
    public Book getBookById(Long id) {
        return bookMapper.findById(id);
    }
    
    public Book getBookByName(String name) {
        return bookMapper.findByName(name);
    }
    
    public boolean createBook(Book book) {
        return bookMapper.insert(book) > 0;
    }
    
    public boolean updateBook(Book book) {
        return bookMapper.update(book) > 0;
    }
    
    public boolean deleteBook(Long id) {
        return bookMapper.deleteById(id) > 0;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public boolean borrowBook(String bookName) {
        Book book = bookMapper.findByName(bookName);
        if (book == null || book.getStock() <= 0) {
            throw new RuntimeException("图书不存在或库存不足");
        }
        return bookMapper.decreaseStock(bookName) > 0;
    }
    
    public boolean returnBook(String bookName) {
        return bookMapper.increaseStock(bookName) > 0;
    }
}
