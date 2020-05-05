package com.github.zhengframework.jpa;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.jpa.a.Work;
import com.github.zhengframework.jpa.b.Work2;
import com.google.inject.Injector;
import com.google.inject.Key;

public class JpaMultiModuleTest {

  public static void test(Injector injector) throws Exception {

    Work work = injector.getInstance(Key.get(Work.class, named("a")));

    work.makeAThing();
    work.makeAThing();
    work.makeAThing();

    System.out.println("There are now " + work.countThings() + " things");

    assert (3 == work.countThings());

    Work2 work2 = injector.getInstance(Key.get(Work2.class, named("b")));

    work2.makeAThing();
    work2.makeAThing();
    work2.makeAThing();
    work2.makeAThing();

    System.out.println("There are now " + work2.countThings() + " things");

    assert (4 == work2.countThings());
  }
}
