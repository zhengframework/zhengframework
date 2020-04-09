package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.environment.Environment;
import com.dadazhishi.zheng.configuration.ex.ConfigurationSourceException;
import com.dadazhishi.zheng.configuration.ex.MissingEnvironmentException;
import java.util.Map;

public interface ConfigurationSource {

  /**
   * @throws ConfigurationSourceException fetch fail
   * @throws IllegalStateException when source was improperly configured
   */
  void init();

  void addListener(ConfigurationSourceListener listener);

  void removeListener(ConfigurationSourceListener listener);

  /**
   * Get configuration
   *
   * @param environment environment to use
   * @return configuration set for {@code environment}
   * @throws MissingEnvironmentException when requested environment couldn't be found
   * @throws IllegalStateException when unable to fetch configuration
   */
  Map<String, String> getConfiguration(Environment environment);

}