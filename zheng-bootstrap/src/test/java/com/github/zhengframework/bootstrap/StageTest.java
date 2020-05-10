package com.github.zhengframework.bootstrap;

import static org.junit.Assert.fail;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.MapConfiguration;
import com.google.inject.Stage;
import java.util.Collections;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class StageTest {

  @Test
  public void test() {
    Configuration configuration =
        new MapConfiguration(Collections.singletonMap("zheng.guice.stage", "DEVELOPMENT"));
    Optional<Stage> stage = configuration.getEnum("zheng.guice.stage", Stage.class);
    if (stage.isPresent()) {
      log.info("stage={}", stage.get());
    } else {
      log.error("not set stage");
    }
  }

  @Test
  public void testFail() {
    Configuration configuration =
        new MapConfiguration(Collections.singletonMap("zheng.guice.stage", "DEV"));
    try {
      Optional<Stage> stage = configuration.getEnum("zheng.guice.stage", Stage.class);
      if (stage.isPresent()) {
        log.info("stage={}", stage.get());
      } else {
        log.error("not set stage");
      }
      fail("stage value is error");
    } catch (IllegalArgumentException ignored) {

    }
  }

  @Test
  public void testNotSet() {
    Configuration configuration = new MapConfiguration(Collections.emptyMap());
    Optional<Stage> stage = configuration.getEnum("zheng.guice.stage", Stage.class);
    if (stage.isPresent()) {
      log.info("stage={}", stage.get());
    } else {
      log.error("not set stage");
    }
  }
}
