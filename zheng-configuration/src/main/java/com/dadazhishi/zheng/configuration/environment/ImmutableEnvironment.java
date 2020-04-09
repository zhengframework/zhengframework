package com.dadazhishi.zheng.configuration.environment;

import java.util.Objects;

public class ImmutableEnvironment implements Environment {

  private final String envName;

  public ImmutableEnvironment(String envName) {
    this.envName = envName;
  }

  @Override
  public String getName() {
    return envName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImmutableEnvironment that = (ImmutableEnvironment) o;
    return envName.equals(that.envName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(envName);
  }
}
