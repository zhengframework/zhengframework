package com.github.zhengframework.jdbc;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@ConfigurationInfo(prefix = "zheng.flyway", supportGroup = true)
public class FlywayConfig implements ConfigurationDefine {

  private boolean enable = true;

  private String[] locations = new String[]{"classpath:db/migration/"};
}
