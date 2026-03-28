package cn.whpu.library.service;

import cn.whpu.library.entity.Borrow;
import cn.whpu.library.mapper.BorrowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BorrowService {
    
    @Autowired
    private BorrowMapper borrowMapper;
    
    public Map<String, Object> getBorrows(String username, String bookName, int pageNum, int pageSize) {
        List<Borrow> allBorrows;
        
        if (username != null && !username.trim().isEmpty() && 
            bookName != null && !bookName.trim().isEmpty()) {
            // 组合查询
            allBorrows = borrowMapper.findByCondition(username, bookName);
        } else if (username != null && !username.trim().isEmpty()) {
            // 只按用户名查询
            allBorrows = borrowMapper.findByUsername(username);
        } else if (bookName != null && !bookName.trim().isEmpty()) {
            // 只按书名查询
            allBorrows = borrowMapper.findByBookName(bookName);
        } else {
            // 查询所有
            allBorrows = borrowMapper.findAll();
        }
        
        // 手动分页
        int total = allBorrows.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        
        List<Borrow> pageData;
        if (fromIndex >= total) {
            pageData = List.of();
        } else {
            pageData = allBorrows.subList(fromIndex, toIndex);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageData);
        result.put("total", total);
        
        return result;
    }
    
    public boolean createBorrow(String username, String bookName) {
        Borrow borrow = new Borrow();
        borrow.setUsername(username);
        borrow.setBookName(bookName);
        return borrowMapper.insert(borrow) > 0;
    }
    
    public boolean returnBook(Long id) {
        return borrowMapper.updateReturnTime(id) > 0;
    }
    
    public boolean deleteBorrow(Long id) {
        return borrowMapper.deleteById(id) > 0;
    }
}
