package com.github.zhengframework.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import com.github.zhengframework.guice.ExposedPrivateModule;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.kohsuke.MetaInfServices;

@MetaInfServices
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
  private Set<String> managedClasses = new HashSet<>();
  @JsonIgnore
  private Map<String, String> properties = new HashMap<>();
  private Class<? extends ExposedPrivateModule> extraModuleClass;
}
