package com.github.zhengframework.shiro;

/*-
 * #%L
 * zheng-shiro
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.matcher.Matchers;
import java.util.Map;
import java.util.Objects;
import org.apache.shiro.SecurityUtils;
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
public class ShiroModule extends ConfigurationAwareModule {


  @Override
  protected void configure() {
    Map<String, ShiroConfig> shiroConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), ShiroConfig.class);
    ShiroConfig shiroConfig = shiroConfigMap.getOrDefault("", new ShiroConfig());

    Ini ini = new Ini();
    ini.loadFromPath(shiroConfig.getIniConfig());
    BasicIniEnvironment environment = new BasicIniEnvironment(ini);
    bind(Environment.class).toInstance(environment);
    DefaultSecurityManager securityManager = (DefaultSecurityManager) environment
        .getSecurityManager();
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
    SecurityUtils.setSecurityManager(securityManager);
    install(new ShiroAopModule());
    bind(ShiroService.class).asEagerSingleton();
  }

}
