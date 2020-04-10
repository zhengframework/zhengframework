package com.github.zhengframework.configuration.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class FileLocator {

  private String fileName;

  private String basePath;

  private String sourceURL;

  private String encoding;

  public FileLocator copy() {
    return FileLocator.builder()
        .fileName(fileName)
        .basePath(basePath)
        .encoding(encoding)
        .sourceURL(sourceURL)
        .build();
  }
}
