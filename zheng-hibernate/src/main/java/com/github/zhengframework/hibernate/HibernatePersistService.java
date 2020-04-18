package com.github.zhengframework.hibernate;

import com.google.common.base.Preconditions;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HibernatePersistService implements Provider<EntityManager>, UnitOfWork,
    PersistService {

  private static final Logger logger = LoggerFactory.getLogger(HibernatePersistService.class);
  private final Configuration configuration;
  private final BootstrapServiceRegistry bootstrapServiceRegistry;
  private final ThreadLocal<EntityManager> entityManager = new ThreadLocal<>();
  private volatile SessionFactory sessionFactory;
  private volatile boolean started;


  @Inject
  public HibernatePersistService(HibernateEntityClassProvider entityClassProvider,
      IntegratorClassScanner integratorScanner, HibernateConfig hibernateConfig) {
    final BootstrapServiceRegistryBuilder builder = new BootstrapServiceRegistryBuilder();
    integratorScanner.accept(builder::applyIntegrator);
    this.bootstrapServiceRegistry = builder.build();

    this.configuration = new Configuration();
    hibernateConfigToMap(hibernateConfig).forEach(configuration::setProperty);
    entityClassProvider.get().forEach(configuration::addAnnotatedClass);
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
      hibernateConfig.getProperties().forEach((s, s2) -> map.put(s.replace("_", "."), s2));
    }
    return map;
  }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  @Override
  public EntityManager get() {
    if (!started) {
      throw new IllegalStateException(
          "HibernatePersistService has not been started or has been stopped.");
    }
    if (!isWorking()) {
      begin();
    }
    EntityManager em = entityManager.get();
    Preconditions.checkState(
        null != em,
        "Requested EntityManager outside work unit. "
            + "Try calling UnitOfWork.begin() first, or use a PersistFilter if you "
            + "are inside a servlet environment.");

    return em;
  }

  public boolean isWorking() {
    return entityManager.get() != null;
  }

  @Override
  public void begin() {
    Preconditions.checkState(
        null == entityManager.get(),
        "Work already begun on this thread. Looks like you have called UnitOfWork.begin() twice"
            + " without a balancing call to end() in between.");

    entityManager.set(sessionFactory.createEntityManager());
  }

  @Override
  public void end() {
    EntityManager em = entityManager.get();

    // Let's not penalize users for calling end() multiple times.
    if (null == em) {
      return;
    }

    try {
      em.close();
    } finally {
      entityManager.remove();
    }
  }

  @Override
  public synchronized void start() {
    logger.info("Starting HibernatePersistService");
    final ServiceRegistry registry = new StandardServiceRegistryBuilder(bootstrapServiceRegistry)
        .applySettings(configuration.getProperties())
        .build();

    this.sessionFactory = configuration.buildSessionFactory(registry);
    logger.info("HibernatePersistServiceStarted");
    started = true;
  }

  @Override
  public void stop() {
    logger.info("Stopping HibernatePersistService");
    sessionFactory.close();
    logger.info("HibernatePersistService stopped");
  }

  @Singleton
  public static class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {

    private final HibernatePersistService emProvider;

    @Inject
    public EntityManagerFactoryProvider(HibernatePersistService emProvider) {
      this.emProvider = emProvider;
    }

    @Override
    public EntityManagerFactory get() {
      assert null != emProvider.sessionFactory;
      return emProvider.sessionFactory;
    }
  }

}
