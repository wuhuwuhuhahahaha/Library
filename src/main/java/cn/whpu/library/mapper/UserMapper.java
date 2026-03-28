package cn.whpu.library.mapper;

import cn.whpu.library.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
    
    @Insert("INSERT INTO user(username, password) VALUES(#{username}, #{password})")
    int insert(User user);
}
