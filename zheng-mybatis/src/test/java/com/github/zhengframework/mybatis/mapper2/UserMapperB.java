package com.github.zhengframework.mybatis.mapper2;

import com.github.zhengframework.mybatis.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapperB {

  User getUser(@Param("userId") String userId);

}
