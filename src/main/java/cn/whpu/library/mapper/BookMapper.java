package cn.whpu.library.mapper;

import cn.whpu.library.entity.Book;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BookMapper {
    
    @Select("SELECT * FROM book")
    List<Book> findAll();
    
    @Select("SELECT * FROM book WHERE name LIKE CONCAT('%', #{keyword}, '%') OR author LIKE CONCAT('%', #{keyword}, '%')")
    List<Book> findByKeyword(String keyword);
    
    @Select("SELECT * FROM book WHERE id = #{id}")
    Book findById(Long id);
    
    @Select("SELECT * FROM book WHERE name = #{name}")
    Book findByName(String name);
    
    @Insert("INSERT INTO book(name, author, stock) VALUES(#{name}, #{author}, #{stock})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Book book);
    
    @Update("UPDATE book SET name=#{name}, author=#{author}, stock=#{stock} WHERE id=#{id}")
    int update(Book book);
    
    @Delete("DELETE FROM book WHERE id = #{id}")
    int deleteById(Long id);
    
    @Update("UPDATE book SET stock = stock - 1 WHERE name = #{bookName} AND stock > 0")
    int decreaseStock(@Param("bookName") String bookName);
    
    @Update("UPDATE book SET stock = stock + 1 WHERE name = #{bookName}")
    int increaseStock(@Param("bookName") String bookName);
}
