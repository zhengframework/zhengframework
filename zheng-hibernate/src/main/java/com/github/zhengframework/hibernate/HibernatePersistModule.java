package com.github.zhengframework.hibernate;

import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.hibernate.HibernatePersistService.EntityManagerFactoryProvider;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import org.aopalliance.intercept.MethodInterceptor;
import org.hibernate.SessionFactory;
import org.hibernate.integrator.spi.Integrator;

/**
 * code base on [guice-persist-hibernate](https://github.com/jcampos8782/guice-persist-hibernate)
 */
public class HibernatePersistModule extends
//    PersistModule
    AbstractModule
    implements ConfigurationAware {

  private com.github.zhengframework.configuration.Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, HibernateConfig> hibernateConfigMap = ConfigurationBeanMapper
        .resolve(configuration, HibernateConfig.class);
    HibernateConfig hibernateConfig = hibernateConfigMap.getOrDefault("", new HibernateConfig());

//    for (Entry<String, HibernateConfig> entry : hibernateConfigMap
//        .entrySet()) {
//      String name = entry.getKey();
//      HibernateConfig hibernateConfig = entry.getValue();
//      install(
//          new HibernatePersistPrivateModule(name.isEmpty() ? null : named(name), hibernateConfig));
//    }

    bind(HibernateConfig.class).toInstance(hibernateConfig);

    String entityPackages = hibernateConfig.getEntityPackages();

    Preconditions
        .checkState(!Strings.isNullOrEmpty(entityPackages), "entityPackages is null or empty");

    String[] strings = entityPackages.split(",");
    HibernateEntityClassProvider entityClassProvider = new PackageScanEntityClassProvider(
        strings);
    bind(HibernateEntityClassProvider.class).toInstance(entityClassProvider);

    bind(HibernatePersistService.class).in(Singleton.class);
    bind(PersistService.class).to(HibernatePersistService.class);
    bind(UnitOfWork.class).to(HibernatePersistService.class);
    bind(EntityManager.class).toProvider(HibernatePersistService.class);
    bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class);

    bind(HibernateService.class).asEagerSingleton();

    this.bindInterceptor(Matchers.annotatedWith(Transactional.class), Matchers.any(),
        getTransactionInterceptor());
    this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class),
        getTransactionInterceptor());

    Multibinder.newSetBinder(binder(), Integrator.class);
  }


  protected MethodInterceptor getTransactionInterceptor() {
    MethodInterceptor txInterceptor = new HibernateTransactionInterceptor();
    requestInjection(txInterceptor);
    return txInterceptor;
  }

  @Override
  public void initConfiguration(
      com.github.zhengframework.configuration.Configuration configuration) {
    this.configuration = configuration;
  }

  @Singleton
  @Inject
  @Provides
  private SessionFactory sessionFactory(HibernatePersistService hibernatePersistService) {
    return hibernatePersistService.getSessionFactory();
  }

}
