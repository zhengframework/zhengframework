package com.github.zhengframework.mybatis;

import com.github.zhengframework.guice.ExposedPrivateModule;
import org.apache.ibatis.jdbc.ScriptRunner;

public class XmlExtraModule extends ExposedPrivateModule {

  @Override
  protected void configure() {
    bind(UserDAO.class).to(UserDAOAImpl.class);
    bind(FooService.class).to(FooServiceMapperAImpl.class);
    bind(ScriptRunner.class).toProvider(ScriptRunnerProvider.class);
    expose(UserDAO.class);
    expose(FooService.class);
    expose(ScriptRunner.class);
  }
}
