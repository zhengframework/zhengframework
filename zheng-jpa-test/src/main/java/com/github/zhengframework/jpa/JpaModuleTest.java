package com.github.zhengframework.jpa;

import com.github.zhengframework.jpa.a.Work;
import com.google.inject.Injector;

public class JpaModuleTest {

  public static void test(Injector injector) throws Exception {
    Work work = injector.getInstance(Work.class);
    work.makeAThing();
    work.makeAThing();
    work.makeAThing();
    System.out.println("There are now " + work.countThings() + " things");
    assert 3 == work.countThings();
  }
}
