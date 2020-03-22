package com.dadazhishi.zheng.jersey;

import com.dadazhishi.zheng.jersey.configuration.JerseyConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletModule;

public class AppServerModule extends AbstractModule {

  private final JerseyConfiguration jerseyConfiguration;

  public AppServerModule(
      JerseyConfiguration jerseyConfiguration) {
    this.jerseyConfiguration = jerseyConfiguration;
  }

  @Override
  protected void configure() {
    install(new ServletModule());
    bind(JerseyConfiguration.class).toInstance(jerseyConfiguration);
  }
}
