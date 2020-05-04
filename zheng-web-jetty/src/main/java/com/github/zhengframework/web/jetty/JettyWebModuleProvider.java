package com.github.zhengframework.web.jetty;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class JettyWebModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new JettyWebModule();
  }

}
