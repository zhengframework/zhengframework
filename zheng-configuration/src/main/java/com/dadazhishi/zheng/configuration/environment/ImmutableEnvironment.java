package com.dadazhishi.zheng.configuration.environment;

public class ImmutableEnvironment implements Environment {

  private final String envName;

  public ImmutableEnvironment(String envName) {
    this.envName = envName;
  }

  @Override
  public String getName() {
    return envName;
  }

}
