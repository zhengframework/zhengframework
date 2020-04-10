package com.github.zhengframework.hibernate;

import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;

public class MyModule extends AbstractModule {

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  public HibernateConfig hibernateConfig() {
    HibernateConfig cfg = new HibernateConfig();
    cfg.setEntityPackages(HibernatePersistModuleTest.class.getPackage().getName());
    cfg.setDriverClassName("org.h2.Driver");
    cfg.setUsername("sa");
    cfg.setUrl("jdbc:h2:mem:test");
    cfg.getProperties().put(DIALECT, "org.hibernate.dialect.H2Dialect");
    cfg.getProperties().put(HBM2DDL_AUTO, "create");
    return cfg;
  }
}
