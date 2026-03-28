package cn.whpu.library.mapper;

import cn.whpu.library.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
    
    @Insert("INSERT INTO user(username, password) VALUES(#{username}, #{password})")
    int insert(User user);
    
    @Select("SELECT * FROM user")
    List<User> findAll();
    
    @Select("SELECT * FROM user WHERE username LIKE CONCAT('%', #{keyword}, '%')")
    List<User> findByKeyword(String keyword);
    
    @Update("UPDATE user SET username = #{username} WHERE id = #{id}")
    int updateUsername(User user);
    
    @Update("UPDATE user SET username = #{username}, password = #{password} WHERE id = #{id}")
    int update(User user);
    
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Long id);
}
