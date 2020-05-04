package com.github.zhengframework.jdbc.migrate;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@NoArgsConstructor
@Data
@ConfigurationInfo(prefix = "zheng.flyway", supportGroup = true)
public class FlywayConfig implements ConfigurationDefine {

  private boolean enable = true;

  private String location = "classpath:db/migration/";
}
