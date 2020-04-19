package com.github.zhengframework.jpa;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.jpa", supportGroup = true)
public class JpaConfig implements ConfigurationDefine {

  private String persistenceUnitName;
  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private String managedClassPackages;
  private Set<String> managedClassSet = new HashSet<>();
  private Map<String, String> properties = new HashMap<>();
  private Set<Class<?>> exposeClassSet = new HashSet<>();// multi support, expose class

}
