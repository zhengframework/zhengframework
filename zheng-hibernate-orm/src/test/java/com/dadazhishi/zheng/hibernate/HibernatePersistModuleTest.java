package com.dadazhishi.zheng.hibernate;

import static org.junit.Assert.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.junit.Before;
import org.junit.Test;

public class HibernatePersistModuleTest {

  private Injector injector;
  private Injector injector2;

  @Before
  public void beforeEach() {
    HibernateConfig hibernateConfig = new HibernateConfig();
    hibernateConfig.setEntityPackages("com.dadazhishi.zheng.hibernate");
    injector = Guice.createInjector(
        new HibernatePersistModule(new TestEntityProvider(), hibernateConfig));
    injector2 = Guice.createInjector(new HibernatePersistModule(null, hibernateConfig)
    );
  }

  @Test(expected = CreationException.class)
  public void failsToIntializeWithoutRequiredProviders() {
    Guice.createInjector(new HibernatePersistModule());
  }

  @Test
  public void initializationSucceedsUsingProviderConstructor() {

    Guice.createInjector(
        new HibernatePersistModule(new TestEntityProvider(), new HibernateConfig()));
  }

  @Test
  public void intializationSucceedsUsingAdditionalModule() {
    Guice.createInjector(new HibernatePersistModule(), new AbstractModule() {
      @Override
      protected void configure() {
        bind(HibernateEntityClassProvider.class).to(TestEntityProvider.class);
        bind(HibernateConfig.class).toInstance(new HibernateConfig());
      }
    });
  }

  @Test
  public void testPersistServiceAndSessionFactoryProviderAreSingleton() {
    final HibernatePersistService persistService = (HibernatePersistService) injector
        .getInstance(PersistService.class);
    final HibernatePersistService hibernatePersistService = injector
        .getInstance(HibernatePersistService.class);
    assertEquals(persistService, hibernatePersistService);
  }

  @Test
  public void testPersistServiceAndSessionFactoryProviderAreSingleton2() {
    final HibernatePersistService persistService = (HibernatePersistService) injector2
        .getInstance(PersistService.class);
    final HibernatePersistService hibernatePersistService = injector2
        .getInstance(HibernatePersistService.class);
    assertEquals(persistService, hibernatePersistService);
  }

  @Test
  public void testUnitOfWorkAndSessionProviderAreSingleton() {
    final HibernateUnitOfWork unitOfWork = (HibernateUnitOfWork) injector
        .getInstance(UnitOfWork.class);
    final HibernateUnitOfWork hibernateUnitOfWork = injector.getInstance(HibernateUnitOfWork.class);
    assertEquals(unitOfWork, hibernateUnitOfWork);
  }

  @Test
  public void testUnitOfWorkAndSessionProviderAreSingleton2() {
    final HibernateUnitOfWork unitOfWork = (HibernateUnitOfWork) injector2
        .getInstance(UnitOfWork.class);
    final HibernateUnitOfWork hibernateUnitOfWork = injector2
        .getInstance(HibernateUnitOfWork.class);
    assertEquals(unitOfWork, hibernateUnitOfWork);
  }

  @Entity
  private static class TestEntity {

    @Id
    long id;
  }

  private static class TestEntityProvider implements HibernateEntityClassProvider {

    @Override
    public List<Class<?>> get() {
      return Arrays.asList(TestEntity.class);
    }
  }

}
