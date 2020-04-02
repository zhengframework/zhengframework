package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationSource;
import com.google.common.base.Joiner;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClassPathConfigurationSource implements AutoConfigurationSource {

  private boolean failOnError = false;

  @Override
  public void init(Map<String, String> properties) {
    failOnError = Boolean.parseBoolean(properties.getOrDefault("failOnError", "false"));
  }

  @Override
  public String[] schemes() {
    return new String[]{"classpath" };
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
      log.info("uri={}", uri);
      String path = uri.getSchemeSpecificPart();
      if (!path.startsWith("/")) {
        path = "/" + path;
      }
      URL resource = ClassPathConfigurationSource.class.getResource(path);
      if (resource != null) {
        try {
          return resource.openStream();
        } catch (IOException e) {
          if (failOnError) {
            throw new RuntimeException("open stream from uri fail, uri=" + uri, e);
          } else {
            log.warn("open stream from uri fail, uri={}", uri, e);
          }
        }
      }
      return new ByteArrayInputStream(new byte[0]);
    };
  }
}
