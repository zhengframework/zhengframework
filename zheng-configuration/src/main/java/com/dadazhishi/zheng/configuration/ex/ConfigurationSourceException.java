package com.github.zhengframework.configuration.ex;

public class ConfigurationSourceException extends RuntimeException {

  public ConfigurationSourceException(String msg, Exception e) {
    super("Unable to fetch configuration source: " + msg, e);
  }
}
