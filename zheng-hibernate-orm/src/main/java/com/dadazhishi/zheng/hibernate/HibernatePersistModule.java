package com.dadazhishi.zheng.hibernate;

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
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import org.aopalliance.intercept.MethodInterceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.integrator.spi.Integrator;

/**
 * Adaptation of the {@link PersistModule} specific to using Hibernate via a {@code SessionFactory}
 * as opposed to the JPA {@code EntityManagerFactory}. This allows for runtime binding of
 * configuration properties (using a {@link HibernateConfig} and programmatic configuration of
 * {@code @Entity} classes (Yay! No XML!). <br /> <br /> Users of this module *must* either<br /> a)
 * Provide {@link HibernateEntityClassProvider} and {@link HibernateConfig} classes to the
 * constructor<br /> OR<br/> b) Provider bindings for those classes via an installed Guice module
 *
 * @author Jason Campos <jcampos8782@gmail.com>
 */
public class HibernatePersistModule extends PersistModule {

  private final HibernateEntityClassProvider entityClassProvider;
  private final HibernateConfig hibernateConfig;

  /**
   * Instantiates module with the specified entity class and property providers. No additional
   * bindings required for this module to function properly.
   */
  public HibernatePersistModule() {
    this.entityClassProvider = null;
    this.hibernateConfig = null;
  }

  public HibernatePersistModule(
      HibernateConfig hibernateConfig) {
    this(null, hibernateConfig);
  }

  public HibernatePersistModule(
      final HibernateEntityClassProvider entityClassProvider,
      HibernateConfig hibernateConfig) {
    this.entityClassProvider = entityClassProvider;
    this.hibernateConfig = Objects.requireNonNull(hibernateConfig);
  }

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
    requireBinding(HibernateEntityClassProvider.class);
    requireBinding(HibernateConfig.class);

    if (hibernateConfig != null) {
      bind(HibernateConfig.class).toInstance(hibernateConfig);
      if (hibernateConfig.getEntityPackages() != null
          && hibernateConfig.getEntityPackages().length > 0) {
        PackageScanEntityClassProvider entityClassProvider = new PackageScanEntityClassProvider(
            hibernateConfig.getEntityPackages());
        bind(HibernateEntityClassProvider.class).toInstance(entityClassProvider);
      }
    }

    if (entityClassProvider != null) {
      bind(HibernateEntityClassProvider.class).toInstance(entityClassProvider);
    }

    // Resolve PersistService, Provider<SessionFactory>, and HibernatePersistService
    // to the same object.
    bind(HibernatePersistService.class).in(Singleton.class);
    bind(PersistService.class).to(HibernatePersistService.class);
    bind(SessionFactory.class).toProvider(HibernatePersistService.class);

    // Resolve UnitOfWork, Provider<Session>,and HibernateUnitOfWork
    // to the same object.
    bind(HibernateUnitOfWork.class).in(Singleton.class);
    bind(UnitOfWork.class).to(HibernateUnitOfWork.class);
    bind(Session.class).toProvider(HibernateUnitOfWork.class);

    // Default to an empty set of integrators
    // @formatter:off
		final List<Integrator> integrators = Collections.emptyList();
		OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<ImmutableSet<? extends Integrator>>(){})
			.setDefault()
			.toInstance(ImmutableSet.copyOf(integrators));



		// @formatter:on
    // Since Session implements EntityManager, bind EntityManager as well in case the user would rather use JPA
    // classes
    bind(EntityManager.class).toProvider(HibernateSessionEntityManagerAdapter.class);
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
      final ImmutableSet<? extends Integrator> integrators) {
    final BootstrapServiceRegistryBuilder builder = new BootstrapServiceRegistryBuilder();
    for (final Integrator integrator : integrators) {
      builder.applyIntegrator(integrator);
    }
    return builder.build();
  }
}
