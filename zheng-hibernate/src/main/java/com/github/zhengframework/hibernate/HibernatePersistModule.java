package com.github.zhengframework.hibernate;

import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.core.ConfigurationAware;
import com.github.zhengframework.hibernate.HibernatePersistService.EntityManagerFactoryProvider;
import com.github.zhengframework.service.ServicesModule;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.persist.PersistModule;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.integrator.spi.Integrator;

// code base on [guice-persist-hibernate](https://github.com/jcampos8782/guice-persist-hibernate)

public class HibernatePersistModule extends PersistModule implements ConfigurationAware {

  private com.github.zhengframework.core.Configuration configuration;


  private Map<String, String> hibernateConfigToMap(HibernateConfig hibernateConfig) {
    Map<String, String> map = new HashMap<>();
    if (hibernateConfig.getDriverClassName() != null) {
      map.put("hibernate.connection.driver_class", hibernateConfig.getDriverClassName());
    }
    if (hibernateConfig.getUrl() != null) {
      map.put("hibernate.connection.url", hibernateConfig.getUrl());
    }
    if (hibernateConfig.getUsername() != null) {
      map.put("hibernate.connection.username", hibernateConfig.getUsername());
    }
    if (hibernateConfig.getPassword() != null) {
      map.put("hibernate.connection.password", hibernateConfig.getPassword());
    } else {
      map.put("hibernate.connection.password", "");
    }
    if (hibernateConfig.getProperties() != null && !hibernateConfig.getProperties().isEmpty()) {
      hibernateConfig.getProperties().forEach(map::put);
    }
    return map;
  }

  @Override
  protected void configurePersistence() {
    install(new ServicesModule());
    Preconditions.checkArgument(configuration != null, "configuration is null");
    HibernateConfig hibernateConfig = ConfigurationBeanMapper
        .resolve(configuration, HibernateConfig.PREFIX, HibernateConfig.class);
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
    final List<Integrator> integrators = Collections.emptyList();
    OptionalBinder
        .newOptionalBinder(binder(), new TypeLiteral<ImmutableSet<? extends Integrator>>() {
        })
        .setDefault()
        .toInstance(ImmutableSet.copyOf(integrators));

    bind(EntityManager.class).toProvider(HibernatePersistService.class);
    bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class);

    bind(HibernateService.class).asEagerSingleton();
  }

  @Override
  protected MethodInterceptor getTransactionInterceptor() {
    final MethodInterceptor txInterceptor = new HibernateTransactionInterceptor();
    requestInjection(txInterceptor);
    return txInterceptor;
  }

  @Singleton
  @Inject
  @Provides
  private SessionFactory sessionFactory(HibernatePersistService hibernatePersistService) {
    return hibernatePersistService.getSessionFactory();
  }


  @Singleton
  @Inject
  @Provides
  private Configuration getHibernateConfiguration(
      final HibernateEntityClassProvider entityClassProvider,
      final HibernateConfig hibernateConfig) {
    final Configuration configuration = new Configuration();
    hibernateConfigToMap(hibernateConfig).forEach(configuration::setProperty);
    entityClassProvider.get().forEach(configuration::addAnnotatedClass);
    return configuration;
  }

  @Provides
  @Singleton
  @Inject
  private BootstrapServiceRegistry getBootstrapServiceRegistry(
      IntegratorClassScanner integratorScanner) {
    final BootstrapServiceRegistryBuilder builder = new BootstrapServiceRegistryBuilder();
    integratorScanner.accept(builder::applyIntegrator);
    return builder.build();
  }

  @Override
  public void initConfiguration(
      com.github.zhengframework.core.Configuration configuration) {
    this.configuration = configuration;
  }

}
