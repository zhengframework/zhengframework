package com.github.zhengframework.jpa;

import com.google.common.base.Preconditions;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import java.util.Iterator;
import java.util.ServiceLoader;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

@Singleton
public class JpaService implements Provider<EntityManager>, UnitOfWork, PersistService {

  private final ThreadLocal<EntityManager> entityManager = new ThreadLocal<>();
  private final PersistenceUnitInfo persistenceUnitInfo;
  private volatile EntityManagerFactory emFactory;

  @Inject
  public JpaService(PersistenceUnitInfo persistenceUnitInfo) {
    this.persistenceUnitInfo = persistenceUnitInfo;
  }

  @Override
  public EntityManager get() {
    if (!isWorking()) {
      begin();
    }

    EntityManager em = entityManager.get();
    Preconditions.checkState(
        null != em,
        "Requested EntityManager outside work unit. "
            + "Try calling UnitOfWork.begin() first, or use com.github.zhengframework.jpa.a PersistFilter if you "
            + "are inside com.github.zhengframework.jpa.a servlet environment.");

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
            + " without com.github.zhengframework.jpa.a balancing call to end() in between.");

    entityManager.set(emFactory.createEntityManager());
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
    Preconditions.checkState(null == emFactory, "Persistence service was already initialized.");
    ServiceLoader<EntityManagerFactoryProvider> load = ServiceLoader
        .load(EntityManagerFactoryProvider.class);
    Iterator<EntityManagerFactoryProvider> iterator = load.iterator();
    if (iterator.hasNext()) {
      EntityManagerFactoryProvider entityManagerFactoryProvider = iterator.next();
      emFactory = entityManagerFactoryProvider.get(persistenceUnitInfo);
    } else {
      throw new IllegalArgumentException("request EntityManagerFactoryProvider Implements");
    }
  }

  @Override
  public synchronized void stop() {
    if (emFactory.isOpen()) {
      emFactory.close();
    }
  }

  @Singleton
  public static class EntityManagerFactoryInternalProvider implements
      Provider<EntityManagerFactory> {

    private final JpaService emProvider;

    @Inject
    public EntityManagerFactoryInternalProvider(JpaService emProvider) {
      this.emProvider = emProvider;
    }

    @Override
    public EntityManagerFactory get() {
      assert null != emProvider.emFactory;
      return emProvider.emFactory;
    }
  }

}