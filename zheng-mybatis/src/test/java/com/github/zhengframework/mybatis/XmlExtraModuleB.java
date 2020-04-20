package com.github.zhengframework.mybatis;

import com.github.zhengframework.guice.ExposedPrivateModule;
import org.apache.ibatis.jdbc.ScriptRunner;

public class XmlExtraModuleB extends ExposedPrivateModule {

  @Override
  protected void configure() {
    bind(UserDAO.class).to(UserDAOBImpl.class);
    bind(FooService.class).to(FooServiceMapperBImpl.class);
    bind(ScriptRunner.class).toProvider(ScriptRunnerProvider.class);
    expose(UserDAO.class);
    expose(FooService.class);
    expose(ScriptRunner.class);
  }
}
