package com.github.zhengframework.configuration;

import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import com.github.zhengframework.configuration.spi.ConfigurationDefine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationInfo(prefix = "zheng.example", supportGroup = true, examples = {
//    @ConfigurationExample(example = ConfigurationDefineExample.class),
//    @ConfigurationExample(groupName = "first", example = ConfigurationDefineExample.class),
//    @ConfigurationExample(groupName = "second", example = ConfigurationDefineExample.class)
})
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
    private String serverName = "abc";
  }
}
