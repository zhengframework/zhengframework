package com.github.zhengframework.shiro.web;

import java.util.Objects;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.apache.shiro.web.env.DefaultWebEnvironment;
import org.apache.shiro.web.env.EnvironmentLoader;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

public class GuiceShiroFilter extends AbstractShiroFilter {

  private WebEnvironment env;

  @Inject
  GuiceShiroFilter(WebEnvironment env) {
    this.env = env;
  }

  @Override
  public void init() {
    DefaultWebEnvironment webEnvironment = (DefaultWebEnvironment) env;
    ServletContext servletContext = Objects
        .requireNonNull(getServletContext(), "servletContext is null");
    webEnvironment.setServletContext(servletContext);
    servletContext.setAttribute(EnvironmentLoader.ENVIRONMENT_ATTRIBUTE_KEY, webEnvironment);
    WebSecurityManager webSecurityManager = Objects
        .requireNonNull(env.getWebSecurityManager(), "WebSecurityManager is null");
    this.setSecurityManager(webSecurityManager);
    this.setStaticSecurityManagerEnabled(true);
    FilterChainResolver resolver = Objects
        .requireNonNull(env.getFilterChainResolver(), "FilterChainResolver is null");
    setFilterChainResolver(resolver);
  }
}
