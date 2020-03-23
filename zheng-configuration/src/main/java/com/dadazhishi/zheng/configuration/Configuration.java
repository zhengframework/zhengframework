package com.dadazhishi.zheng.configuration;

import java.util.Map;
import java.util.Set;

public interface Configuration extends Map<String, String> {


  Configuration getConfiguration(String namespace);

  Set<Configuration> getConfigurationSet(String namespace);

  Map<String, Configuration> getConfigurationMap(String namespace);

}
