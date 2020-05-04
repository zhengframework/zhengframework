package com.github.zhengframework.mongodb;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class MongodbModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new MongodbModule();
  }
}
