package com.github.zhengframework.hibernate;

import static org.junit.Assert.assertEquals;

import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class HibernatePersistModuleTest {


  @Test(expected = CreationException.class)
  public void failsToInitializedWithoutRequiredProviders() {
    Guice.createInjector(new HibernatePersistModule());
  }

  @Test
  public void example() throws Exception {
    Injector injector = Guice.createInjector(new MyModule(), new HibernatePersistModule());
    injector.getInstance(HibernateService.class).start();
    Work work = injector.getInstance(Work.class);

    work.makeAThing();
    work.makeAThing();
    work.makeAThing();

    System.out.println("There are now " + work.countThings() + " things");

    assertEquals(3, work.countThings());
    // Without this, the program will not exit
//    injector.getInstance(EntityManagerFactory.class).close();
    injector.getInstance(HibernateService.class).stop();
  }

}
