package com.github.zhengframework.hibernate;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.hibernate", supportGroup = true)
public class HibernateConfig implements ConfigurationDefine {

  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private String entityPackages;
  private Map<String, String> properties = new HashMap<>();
  private Collection<Class<?>> classCollection = new HashSet<>();// multi hibernate support, expose object

}
