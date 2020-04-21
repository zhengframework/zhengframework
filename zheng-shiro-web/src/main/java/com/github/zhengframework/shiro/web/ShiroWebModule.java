package com.github.zhengframework.shiro.web;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.web.PathUtils;
import com.google.common.base.Preconditions;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletModule;
import java.util.Map;
import java.util.Objects;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.config.Ini;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.support.DefaultEventBus;
import org.apache.shiro.guice.aop.ShiroAopModule;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;

/**
 * https://shiro.apache.org/configuration.html https://shiro.apache.org/web.html#Web-configuration
 */
public class ShiroWebModule extends ServletModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configureServlets() {

    Preconditions.checkArgument(configuration != null, "configuration is null");
    Map<String, ShiroWebConfig> shiroConfigMap = ConfigurationBeanMapper
        .resolve(configuration, ShiroWebConfig.class);
    ShiroWebConfig shiroConfig = shiroConfigMap.getOrDefault("", new ShiroWebConfig());

    Ini ini = new Ini();
    ini.loadFromPath(shiroConfig.getIniConfig());

    IniWebEnvironment environment = new IniWebEnvironment();
    environment.setIni(ini);
    environment.init();
    bind(WebEnvironment.class).toInstance(environment);

    FilterChainResolver filterChainResolver = environment.getFilterChainResolver();
    requestInjection(filterChainResolver);
    bind(FilterChainResolver.class).toInstance(filterChainResolver);

    DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) environment
        .getWebSecurityManager();
    bind(WebSecurityManager.class).toInstance(securityManager);
    bind(SecurityManager.class).toInstance(securityManager);

    RememberMeManager rememberMeManager = securityManager.getRememberMeManager();
    if (rememberMeManager != null) {
      requestInjection(rememberMeManager);
      bind(RememberMeManager.class).toInstance(rememberMeManager);
    }
    SubjectDAO subjectDAO = securityManager.getSubjectDAO();
    if (subjectDAO != null) {
      requestInjection(subjectDAO);
      bind(SubjectDAO.class).toInstance(subjectDAO);
    }
    SubjectFactory subjectFactory = securityManager.getSubjectFactory();
    if (subjectFactory != null) {
      requestInjection(subjectFactory);
      bind(SubjectFactory.class).toInstance(subjectFactory);
    }
    Authenticator authenticator = securityManager.getAuthenticator();
    if (authenticator != null) {
      requestInjection(authenticator);
      bind(Authenticator.class).toInstance(authenticator);
    }
    Authorizer authorizer = securityManager.getAuthorizer();
    if (authorizer != null) {
      requestInjection(authorizer);
      bind(Authorizer.class).toInstance(authorizer);
    }
    CacheManager cacheManager = securityManager.getCacheManager();
    if (cacheManager != null) {
      requestInjection(cacheManager);
      bind(CacheManager.class).toInstance(cacheManager);
    }
    EventBus eventBus = securityManager.getEventBus();
    if (eventBus != null) {
      requestInjection(eventBus);
    } else {
      securityManager.setEventBus(new DefaultEventBus());
    }
    bind(EventBus.class).toInstance(securityManager.getEventBus());
    bindListener(Matchers.any(), new SubscribedEventTypeListener());
    bindListener(Matchers.any(), new EventBusAwareTypeListener());

    for (Realm realm : Objects.requireNonNull(securityManager.getRealms())) {
      requestInjection(realm);
    }
    SessionManager sessionManager = securityManager.getSessionManager();
    if (sessionManager == null) {
      securityManager.setSessionManager(new ServletContainerSessionManager());
    }

    bind(GuiceShiroFilter.class).asEagerSingleton();
    String path = PathUtils.fixPath(shiroConfig.getPath());
    filter(path + "/*").through(GuiceShiroFilter.class);
    install(new ShiroAopModule());
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}