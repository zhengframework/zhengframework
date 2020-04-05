package com.dadazhishi.zheng.configuration.io;

import java.net.URL;
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

  private URL sourceURL;

  private String encoding;
}
