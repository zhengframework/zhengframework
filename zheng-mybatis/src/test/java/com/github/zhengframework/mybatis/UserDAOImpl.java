package com.github.zhengframework.mybatis;

import javax.inject.Inject;
import org.apache.ibatis.session.SqlSession;

public class UserDAOImpl implements UserDAO {

  private final SqlSession session;

  @Inject
  public UserDAOImpl(SqlSession session) {
    this.session = session;
  }

  @Override
  public User getUser(String userId) {
    return session.selectOne("com.github.zhengframework.mybatis.mapper.UserMapper.getUser", userId);
  }
}
