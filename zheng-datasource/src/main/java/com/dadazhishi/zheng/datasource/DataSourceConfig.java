package com.dadazhishi.zheng.datasource;

import java.util.Properties;

public class DataSourceConfig {

  private String username;
  private String password;
  private String driverClassName;
  private String jdbcUrl;
  private Properties dataSourceProperties;

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

  public String getJdbcUrl() {
    return jdbcUrl;
  }

  public void setJdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  public Properties getDataSourceProperties() {
    return dataSourceProperties;
  }

  public void setDataSourceProperties(Properties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  public void addDataSourceProperty(String propertyName, Object value) {
    if (this.dataSourceProperties == null) {
      this.dataSourceProperties = new Properties();
    }
    this.dataSourceProperties.put(propertyName, value);
  }
}
