package com.github.zhengframework.mongodb;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.mongodb", supportGroup = true)
public class MongodbConfig implements ConfigurationDefine {

  private String url;
}
