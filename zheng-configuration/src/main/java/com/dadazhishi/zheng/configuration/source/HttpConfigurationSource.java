package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationSource;
import com.google.common.base.Joiner;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public class HttpConfigurationSource implements AutoConfigurationSource {

  @Override
  public String[] schemes() {
    return new String[]{"http", "https"};
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
      try {
        return uri.toURL().openStream();
      } catch (IOException e) {
        throw new RuntimeException("uri to url fail, uri=" + uri, e);
      }
    };
  }
}
