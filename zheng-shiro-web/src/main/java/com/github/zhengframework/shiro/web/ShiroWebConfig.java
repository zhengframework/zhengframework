package com.github.zhengframework.shiro.web;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.shiro")
public class ShiroWebConfig implements ConfigurationDefine {

  private String iniConfig = "classpath:shiro.ini";

  private String path;
}
