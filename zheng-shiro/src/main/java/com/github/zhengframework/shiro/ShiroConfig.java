package com.github.zhengframework.shiro;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.shiro")
public class ShiroConfig implements ConfigurationDefine {

  String iniConfig = "classpath:shiro.ini";
}
