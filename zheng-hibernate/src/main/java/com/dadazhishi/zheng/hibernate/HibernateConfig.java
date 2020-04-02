package com.dadazhishi.zheng.hibernate;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HibernateConfig {

  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private String entityPackages;
  private Map<String, String> properties = new HashMap<>();

  public void addProperty(String propertyName, String value) {
    if (this.properties == null) {
      this.properties = new HashMap<>();
    }
    this.properties.put(propertyName, value);
  }
}
