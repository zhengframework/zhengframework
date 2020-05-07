package com.github.zhengframework.jpa;

import com.github.zhengframework.jpa.a.Work;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaModuleTest {

  public static void test(Injector injector) throws Exception {
    Work work = injector.getInstance(Work.class);
    work.makeAThing();
    work.makeAThing();
    work.makeAThing();
    log.info("There are now {} things", work.countThings());
    assert 3 == work.countThings();
  }
}
