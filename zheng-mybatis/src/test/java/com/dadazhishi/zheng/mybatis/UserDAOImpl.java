package com.dadazhishi.zheng.mybatis;

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
    return session.selectOne("com.dadazhishi.zheng.mybatis.UserMapper.getUser", userId);
  }
}
