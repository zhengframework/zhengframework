package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.annotation.ConfigurationType;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.name.Names;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ConfigurationModule extends AbstractModule {

  private final Configuration configuration = new ConfigurationImpl();
  private String[] configurationPackages;
  private boolean enableConfigurationScan = true;

  public ConfigurationModule(ConfigurationSource... configurationSources) {
    for (ConfigurationSource configurationSource : configurationSources) {
      configuration.putAll(configurationSource.read());
    }
  }

  public ConfigurationModule(Configuration configuration) {
    this.configuration.putAll(configuration);
  }

  public ConfigurationModule(Configuration configuration,
      AbstractModule abstractModule) {

  }

  @Override
  protected void configure() {
    OptionalBinder.newOptionalBinder(binder(), Configuration.class)
        .setDefault().toInstance(configuration);
    ConfigurationMapper mapper = new ConfigurationMapper();
    bind(ConfigurationMapper.class).toInstance(mapper);
    Names.bindProperties(binder(), System.getProperties());
    Names.bindProperties(binder(), configuration);

    if (isEnableConfigurationScan()) {
      List<Class<?>> classList = scan();
      for (Class<?> aClass : classList) {
        com.dadazhishi.zheng.configuration.annotation.Configuration annotation = aClass
            .getAnnotation(
                com.dadazhishi.zheng.configuration.annotation.Configuration.class);
        if (annotation.type() == ConfigurationType.BEAN) {
          if (annotation.namespace().isEmpty()) {
            bind(aClass).toProvider(new ConfigurationObjectProvider(
                mapper, configuration, aClass)).asEagerSingleton();
          } else {
            bind(aClass).toProvider(new ConfigurationObjectProvider(
                mapper, configuration.getConfiguration(annotation.namespace()), aClass))
                .asEagerSingleton();
          }
        } else if (annotation.type() == ConfigurationType.SET) {

          if (annotation.namespace().isEmpty()) {
            throw new RuntimeException("Configuration type[LIST] need namespace not empty");
          } else {
            Set<Configuration> configurationList = configuration
                .getConfigurationSet(annotation.namespace());
            for (Configuration configuration1 : configurationList) {
              Multibinder.newSetBinder(binder(), aClass).addBinding()
                  .toProvider(new ConfigurationObjectProvider(
                      mapper, configuration1, aClass)).asEagerSingleton();
            }
          }
        } else if (annotation.type() == ConfigurationType.MAP) {

          if (annotation.namespace().isEmpty()) {
            throw new RuntimeException("Configuration type[LIST] need namespace not empty");
          } else {
            Map<String, Configuration> configurationMap = configuration
                .getConfigurationMap(annotation.namespace());
            for (Entry<String, Configuration> entry : configurationMap
                .entrySet()) {
              MapBinder.newMapBinder(binder(), String.class, aClass).addBinding(entry.getKey())
                  .toProvider(new ConfigurationObjectProvider(
                      mapper, entry.getValue(), aClass)).asEagerSingleton();
            }
          }
        }

      }
    }
  }

  public void setConfigurationPackages(String... configurationPackages) {
    this.configurationPackages = configurationPackages;
  }

  private List<Class<?>> scan() {
    if (configurationPackages == null) {
      configurationPackages = new String[0];
    }
    String name = com.dadazhishi.zheng.configuration.annotation.Configuration.class.getName();
    final List<Class<?>> entityClasses = new ArrayList<>();
    ScanResult scanResult = new ClassGraph()
        .whitelistPackages(configurationPackages)
        .enableClassInfo()
        .enableAnnotationInfo()
        .scan();
    for (io.github.classgraph.ClassInfo classInfo : scanResult
        .getClassesWithAnnotation(name)) {
      entityClasses.add(classInfo.loadClass());
    }
    return entityClasses;
  }

  public boolean isEnableConfigurationScan() {
    return enableConfigurationScan;
  }

  public void setEnableConfigurationScan(boolean enableConfigurationScan) {
    this.enableConfigurationScan = enableConfigurationScan;
  }
}
