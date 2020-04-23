package com.github.zhengframework.jdbc;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@ConfigurationInfo(prefix = "zheng.jdbi", supportGroup = true)
public class JdbiConfig implements ConfigurationDefine {

  private boolean enable = true;

}
