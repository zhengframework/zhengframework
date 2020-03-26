package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationSource;
import com.google.common.base.Joiner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class FileConfigurationSource implements AutoConfigurationSource {

  public static final String FILE_BASH_PATH = "file.bash-path";
  private String basePath;

  public FileConfigurationSource(String basePath) {
    this.basePath = basePath;
  }

  public FileConfigurationSource() {
  }

  @Override
  public String[] schemes() {
    return new String[]{"file"};
  }

  @Override
  public void init(Map<String, String> properties) {
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
      File file;
      if (basePath != null) {
        file = new File(basePath, uri.getPath());
      } else {
        file = new File(uri.getPath());
      }
      if (file.exists() && file.canRead()) {
        try {
          return new FileInputStream(file);
        } catch (FileNotFoundException e) {
          throw new RuntimeException("file not found or cannot read, file=" + file.getPath());
        }
      } else {
        throw new RuntimeException("file not found or cannot read, file=" + file.getPath());
      }
    };
  }
}
