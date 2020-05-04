package com.github.zhengframework.rabbitmq;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class RabbitMQModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new RabbitMQModule();
  }
}
