package com.github.zhengframework.webjars;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.webjars")
public class WebjarsConfig implements ConfigurationDefine {

  private String basePath = "/webjars";
  private boolean disableCache = false;

}
