package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.environment.Environment;
import com.dadazhishi.zheng.configuration.ex.ConfigurationSourceException;
import com.dadazhishi.zheng.configuration.ex.MissingEnvironmentException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FallbackConfigurationSource implements ConfigurationSource {

  private final List<ConfigurationSource> sources = new ArrayList<>();

  public FallbackConfigurationSource(ConfigurationSource... sources) {
    Objects.requireNonNull(sources);

    for (ConfigurationSource source : sources) {
      this.sources.add(Objects.requireNonNull(source));
    }
  }

  public FallbackConfigurationSource(Iterator<ConfigurationSource> sources) {
    Objects.requireNonNull(sources).forEachRemaining(configurationSource -> {
      FallbackConfigurationSource.this.sources.add(Objects.requireNonNull(configurationSource));
    });
  }

  public FallbackConfigurationSource(Iterable<ConfigurationSource> sources) {
    Objects.requireNonNull(sources).forEach(configurationSource -> {
      FallbackConfigurationSource.this.sources.add(Objects.requireNonNull(configurationSource));
    });
  }

  @Override
  public void init() {
    boolean atLeastOneSuccess = false;
    for (ConfigurationSource source : sources) {
      try {
        source.init();
        atLeastOneSuccess = true;
      } catch (IllegalStateException | ConfigurationSourceException e) {
        // NOP
      }
    }
    if (!atLeastOneSuccess) {
      throw new IllegalStateException("Unable to initialize any of the underlying sources");
    }
  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    boolean allMissEnvironment = true;
    for (ConfigurationSource source : sources) {
      try {
        return source.getConfiguration(environment);
      } catch (MissingEnvironmentException e) {
        // NOP
      } catch (IllegalStateException e) {
        allMissEnvironment = false;
      }
    }
    if (allMissEnvironment) {
      throw new MissingEnvironmentException(environment.getName());
    }
    throw new IllegalStateException();
  }
}
