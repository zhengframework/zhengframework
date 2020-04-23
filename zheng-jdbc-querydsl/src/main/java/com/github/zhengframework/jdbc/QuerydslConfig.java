package com.github.zhengframework.jdbc;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@ConfigurationInfo(prefix = "zheng.querydsl", supportGroup = true)
public class QuerydslConfig implements ConfigurationDefine {

  private boolean enable = true;

}
