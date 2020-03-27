package com.dadazhishi.zheng.hibernate;

import java.util.HashMap;
import java.util.Map;

public class HibernateConfig {

  private String username;
  private String[] entityPackages;
  private String password;
  private String driverClassName;
  private String url;
  private Map<String, String> properties;

  public String[] getEntityPackages() {
    return entityPackages;
  }

  public void setEntityPackages(String... entityPackages) {
    this.entityPackages = entityPackages;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  public void addProperty(String propertyName, String value) {
    if (this.properties == null) {
      this.properties = new HashMap<>();
    }
    this.properties.put(propertyName, value);
  }
}
