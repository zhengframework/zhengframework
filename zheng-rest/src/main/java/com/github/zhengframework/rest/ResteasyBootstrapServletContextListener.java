package com.github.zhengframework.rest;

/*-
 * #%L
 * zheng-rest
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

public class ResteasyBootstrapServletContextListener extends ResteasyBootstrap
    implements ServletContextListener {

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
    final ResteasyDeployment deployment =
        (ResteasyDeployment) context.getAttribute(ResteasyDeployment.class.getName());
    final Registry registry = deployment.getRegistry();
    final ResteasyProviderFactory providerFactory = deployment.getProviderFactory();
    final ModuleProcessor processor = new ModuleProcessor(registry, providerFactory);
    Injector injector = parentInjector;
    processor.processInjector(injector);
    // load parent injectors
    while (injector.getParent() != null) {
      injector = injector.getParent();
      processor.processInjector(injector);
    }
  }
}
