package com.github.zhengframework.mybatis;

import javax.inject.Inject;
import org.apache.ibatis.session.SqlSession;

public class UserDAOAImpl implements UserDAO {

  private final SqlSession session;

  @Inject
  public UserDAOAImpl(SqlSession session) {
    this.session = session;
  }

  @Override
  public User getUser(String userId) {
    return session
        .selectOne("com.github.zhengframework.mybatis.mapper2.UserMapperA.getUser", userId);
  }
}
