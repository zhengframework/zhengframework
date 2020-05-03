package com.github.zhengframework.rest;

import com.google.inject.Injector;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.jboss.resteasy.plugins.guice.ModuleProcessor;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class ResteasyBootstrapServletContextListener extends ResteasyBootstrap implements
    ServletContextListener {

  @Inject
  private Injector parentInjector = null;

  @Inject
  public ResteasyBootstrapServletContextListener(Injector parentInjector) {
    this.parentInjector = parentInjector;
  }

  @Override
  public void contextInitialized(final ServletContextEvent event) {
    super.contextInitialized(event);
    final ServletContext context = event.getServletContext();
    final ResteasyDeployment deployment = (ResteasyDeployment) context
        .getAttribute(ResteasyDeployment.class.getName());
    final Registry registry = deployment.getRegistry();
    final ResteasyProviderFactory providerFactory = deployment.getProviderFactory();
    final ModuleProcessor processor = new ModuleProcessor(registry, providerFactory);
    Injector injector=parentInjector;
    processor.processInjector(injector);
    //load parent injectors
    while (injector.getParent() != null) {
      injector = injector.getParent();
      processor.processInjector(injector);
    }
  }

}
