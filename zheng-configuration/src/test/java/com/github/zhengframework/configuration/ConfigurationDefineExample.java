package com.github.zhengframework.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.zhengframework.configuration.ConfigurationDefineExample.DefaultConfigurationDefineExample;
import com.github.zhengframework.configuration.ConfigurationDefineExample.FirstConfigurationDefineExample;
import com.github.zhengframework.configuration.ConfigurationDefineExample.SecondConfigurationDefineExample;
import com.github.zhengframework.configuration.annotation.ConfigurationExample;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationInfo(prefix = "zheng.example", supportGroup = true, examples = {
    @ConfigurationExample(example = DefaultConfigurationDefineExample.class),
    @ConfigurationExample(groupName = "first", example = FirstConfigurationDefineExample.class),
    @ConfigurationExample(groupName = "second", example = SecondConfigurationDefineExample.class)
})
@Data
@NoArgsConstructor
public class ConfigurationDefineExample implements ConfigurationDefine {

  private String basePath = "/base";

  private boolean enableWeb = true;

  @JsonIgnore
  private Map<String, Integer> properties = new HashMap<>();

  private JettyConfig jettyConfig = new JettyConfig();

  private List<Integer> integerList = new ArrayList<>();

  @Data
  @NoArgsConstructor
  public static class JettyConfig {

    private int port = 8080;
    private String serverName = "abc";
  }

  static class DefaultConfigurationDefineExample extends ConfigurationDefineExample {

    public DefaultConfigurationDefineExample() {
      Map<String, Integer> properties = new HashMap<>();
      properties.put("a.1", 1);
      properties.put("b.2", 2);
      setProperties(properties);
      getIntegerList().addAll(Arrays.asList(1, 2, 3));
    }
  }

  static class FirstConfigurationDefineExample extends ConfigurationDefineExample {

    public FirstConfigurationDefineExample() {
      Map<String, Integer> properties = new HashMap<>();
      properties.put("aFirst", 1);
      properties.put("bFirst", 2);
      setProperties(properties);
      getIntegerList().addAll(Arrays.asList(0, 1, 2, 3));
    }
  }

  static class SecondConfigurationDefineExample extends ConfigurationDefineExample {

    public SecondConfigurationDefineExample() {
      Map<String, Integer> properties = new HashMap<>();
      properties.put("aSecond", 1);
      properties.put("bSecond", 2);
      setProperties(properties);
      getIntegerList().addAll(Arrays.asList(1, 2, 3, 4));
    }
  }
}
