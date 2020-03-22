package com.dadazhishi.zheng.jersey.jetty;

import org.eclipse.jetty.servlet.ServletContextHandler;

public interface ServletContextHandlerConfigurer {

  void configure(ServletContextHandler servletContextHandler);

  class DefaultServletContextHandlerConfigurer implements ServletContextHandlerConfigurer {

    @Override
    public void configure(ServletContextHandler servletContextHandler) {

    }
  }

}
