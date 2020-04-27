package com.github.zhengframework.test;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WithZhengApplication {

  String configFile() default "";

  Class<? extends Configuration> configurationClass() default NotDefineConfiguration.class;

  String[] arguments() default {};

  Class<? extends Module>[] moduleClass() default {};

  Class<? extends Module>[] excludeModuleClass() default {};

  Class<? extends ModuleProvider>[] excludeModuleProviderClass() default {};

  boolean autoLoadModule() default true;

  class NotDefineConfiguration implements Configuration {

    @Override
    public Optional<String> get(String key) {
      return Optional.empty();
    }

    @Override
    public Set<String> keySet() {
      return null;
    }

    @Override
    public Map<String, String> asMap() {
      return null;
    }

    @Override
    public Configuration prefix(String prefix) {
      return null;
    }

    @Override
    public List<Configuration> prefixList(String prefix) {
      return null;
    }

    @Override
    public Set<Configuration> prefixSet(String prefix) {
      return null;
    }

    @Override
    public Map<String, Configuration> prefixMap(String prefix) {
      return null;
    }
  }
}
