package com.github.zhengframework.mybatis;

import javax.inject.Inject;
import org.apache.ibatis.session.SqlSession;

public class UserDAOBImpl implements UserDAO {

  private final SqlSession session;

  @Inject
  public UserDAOBImpl(SqlSession session) {
    this.session = session;
  }

  @Override
  public User getUser(String userId) {
    return session
        .selectOne("com.github.zhengframework.mybatis.mapper2.UserMapperB.getUser", userId);
  }
}
