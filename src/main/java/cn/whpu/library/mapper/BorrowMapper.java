package cn.whpu.library.mapper;

import cn.whpu.library.entity.Borrow;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BorrowMapper {
    
    @Select("SELECT * FROM borrow")
    List<Borrow> findAll();
    
    @Select("SELECT * FROM borrow WHERE user_name = #{username}")
    List<Borrow> findByUsername(String username);
    
    @Select("SELECT * FROM borrow WHERE book_name = #{bookName}")
    List<Borrow> findByBookName(String bookName);
    
    @Select("SELECT * FROM borrow WHERE user_name LIKE CONCAT('%', #{username}, '%') AND book_name LIKE CONCAT('%', #{bookName}, '%')")
    List<Borrow> findByCondition(@Param("username") String username, @Param("bookName") String bookName);
    
    @Insert("INSERT INTO borrow(user_name, book_name, borrow_time) VALUES(#{username}, #{bookName}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Borrow borrow);
    
    @Update("UPDATE borrow SET return_time = NOW() WHERE id = #{id}")
    int updateReturnTime(Long id);
    
    @Delete("DELETE FROM borrow WHERE id = #{id}")
    int deleteById(Long id);
}
