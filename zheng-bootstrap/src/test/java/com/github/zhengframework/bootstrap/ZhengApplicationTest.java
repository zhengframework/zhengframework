package com.github.zhengframework.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ZhengApplicationTest {

  @Test
  public void start() throws Exception {
    ZhengApplication zhengApplication = ZhengApplicationBuilder.create().build();
    zhengApplication.start();
    GuiceConfig guiceConfig = zhengApplication.getInjector().getInstance(GuiceConfig.class);
    log.info("stage={}", guiceConfig.getStage());
    zhengApplication.stop();
  }
}
