package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.spi.ConfigurationDefine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfigurationDefineExample implements ConfigurationDefine {

  public static final String PREFIX = "zheng.example";

  private String basePath = "/base";

  private boolean enableWeb = true;

  private Map<String, String> properties = new HashMap<>();

  private JettyConfig jettyConfig = new JettyConfig();

  private List<Integer> integerList = new ArrayList<>();

  @Data
  @NoArgsConstructor
  public static class JettyConfig {

    private int port = 8080;
    private String serverName = null;
  }
}
