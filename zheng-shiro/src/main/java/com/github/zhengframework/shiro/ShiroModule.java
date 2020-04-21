package com.github.zhengframework.shiro;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import java.util.Map;
import java.util.Objects;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.config.Ini;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.support.DefaultEventBus;
import org.apache.shiro.guice.aop.ShiroAopModule;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;

/**
 * https://shiro.apache.org/configuration.html
 */
public class ShiroModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    Map<String, ShiroConfig> shiroConfigMap = ConfigurationBeanMapper
        .resolve(configuration, ShiroConfig.class);
    ShiroConfig shiroConfig = shiroConfigMap.getOrDefault("", new ShiroConfig());

    Ini ini = new Ini();
    ini.loadFromPath(shiroConfig.getIniConfig());
    BasicIniEnvironment environment = new BasicIniEnvironment(ini);
    bind(Environment.class).toInstance(environment);
    if (environment.getSecurityManager() instanceof DefaultSecurityManager) {
      DefaultSecurityManager securityManager = (DefaultSecurityManager) environment
          .getSecurityManager();
      requestInjection(securityManager);
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
      if (sessionManager != null) {
        requestInjection(sessionManager);
      } else {
        securityManager.setSessionManager(new DefaultSessionManager());
      }
      bind(SessionManager.class).toInstance(sessionManager);
      bind(SecurityManager.class).toInstance(securityManager);

      install(new ShiroAopModule());
      bind(ShiroService.class).asEagerSingleton();
    }
  }


  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}