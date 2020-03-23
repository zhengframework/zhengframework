package com.dadazhishi.zheng.configuration;

import java.util.List;
import java.util.Map;

public interface Configuration extends Map<String, String> {


  Configuration getConfiguration(String namespace);

  List<Configuration> getConfigurationList(String namespace);

  Map<String, Configuration> getConfigurationMap(String namespace);

}
