package com.github.zhengframework.jdbc.dbutils;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@ConfigurationInfo(prefix = "zheng.dbutils", supportGroup = true)
public class CommonsDbUtilsConfig implements ConfigurationDefine {

  private boolean enable = true;

}
