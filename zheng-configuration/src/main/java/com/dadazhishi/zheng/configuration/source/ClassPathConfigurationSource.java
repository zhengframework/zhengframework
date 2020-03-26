package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationSource;
import com.google.common.base.Joiner;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public class ClassPathConfigurationSource implements AutoConfigurationSource {

  @Override
  public String[] schemes() {
    return new String[]{"classpath"};
  }

  @Override
  public Supplier<InputStream> getSource(URI uri) {
    String scheme = uri.getScheme();
    Optional<String> any = Arrays.stream(schemes()).filter(s -> s.equalsIgnoreCase(scheme))
        .findAny();
    if (!any.isPresent()) {
      throw new RuntimeException(
          "invalid schema, support schema: " + Joiner.on(",").join(schemes()));
    }
    return () -> {
      String path = uri.getPath();
      return ClassPathConfigurationSource.class
          .getResourceAsStream(path);
    };
  }
}
