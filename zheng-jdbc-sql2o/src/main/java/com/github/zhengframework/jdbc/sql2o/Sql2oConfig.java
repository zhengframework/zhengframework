package com.github.zhengframework.jdbc.sql2o;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@NoArgsConstructor
@Data
@ConfigurationInfo(prefix = "zheng.sql2o", supportGroup = true)
public class Sql2oConfig implements ConfigurationDefine {

  private boolean enable = true;

}
