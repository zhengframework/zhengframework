package com.dadazhishi.zheng.hibernate;

import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.junit.Assert.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.persist.Transactional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.junit.Test;

public class HibernatePersistModuleTest {


  @Test(expected = CreationException.class)
  public void failsToInitializedWithoutRequiredProviders() {
    Guice.createInjector(new HibernatePersistModule());
  }

  @Test
  public void example() {
    Injector injector = Guice.createInjector(new MyModule(), new HibernatePersistModule());

    Work work = injector.getInstance(Work.class);

    work.makeAThing();
    work.makeAThing();
    work.makeAThing();

    System.out.println("There are now " + work.countThings() + " things");

    assertEquals(3, work.countThings());
    // Without this, the program will not exit
    injector.getInstance(EntityManagerFactory.class).close();

  }

  @Entity(name = "Thing")
  public static class Thing {

    @Id
    private UUID id = UUID.randomUUID();
    private String name;

    public Thing(String name) {
      setName(name);
    }

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class Work {

    private final EntityManager entityManager;

    @Inject
    public Work(EntityManager entityManager) {

      this.entityManager = entityManager;
    }

    @Transactional
    public void makeAThing() {
      entityManager.persist(new Thing("Thing " + Math.random()));
    }

    @Transactional
    public long countThings() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Long> cq = cb.createQuery(Long.class);
      cq.select(cb.count(cq.from(Thing.class)));
      return entityManager.createQuery(cq).getSingleResult();
    }
  }

  public static class MyModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public HibernateConfig hibernateConfig() {
      HibernateConfig cfg = new HibernateConfig();
      cfg.setEntityPackages(HibernatePersistModuleTest.class.getPackage().getName());
      cfg.setDriverClassName("org.h2.Driver");
      cfg.setUsername("sa");
      cfg.setUrl("jdbc:h2:mem:test");
      cfg.getProperties().put(DIALECT, "org.hibernate.dialect.H2Dialect");
      cfg.getProperties().put(HBM2DDL_AUTO, "create");
      return cfg;
    }
  }

}
