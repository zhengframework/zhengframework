package com.github.zhengframework.mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper2 {

  @Select("SELECT * FROM users WHERE id = #{userId}")
  User getUser(@Param("userId") String userId);

}
