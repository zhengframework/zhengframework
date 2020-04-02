package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationSource;
import com.google.common.base.Joiner;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileConfigurationSource implements AutoConfigurationSource {

  public static final String FILE_BASH_PATH = "file.basePath";
  private String basePath;
  private boolean failOnError = false;

  public FileConfigurationSource() {
  }

  @Override
  public String[] schemes() {
    return new String[]{"file" };
  }

  @Override
  public void init(Map<String, String> properties) {
    failOnError = Boolean.parseBoolean(properties.getOrDefault("failOnError", "false"));
    basePath = properties.get(FILE_BASH_PATH);
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
      File file;
      if (basePath != null) {
        file = new File(basePath, uri.getSchemeSpecificPart());
      } else {
        file = new File(uri.getSchemeSpecificPart());
      }
      if (file.exists() && file.canRead()) {
        try {
          return new FileInputStream(file);
        } catch (FileNotFoundException e) {
          if (failOnError) {
            throw new RuntimeException("file not found or cannot read, file=" + file, e);
          } else {
            log.warn("file not found or cannot read, file={}", file, e);
          }
        }
      } else {
        log.warn("file not found or cannot read, file={}", file);
      }
      return new ByteArrayInputStream(new byte[0]);
    };
  }
}
