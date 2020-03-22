package com.dadazhishi.zheng.jersey.jetty;

import com.dadazhishi.zheng.jersey.AppServer;
import com.dadazhishi.zheng.jersey.GuiceJerseyResourceConfig;
import com.dadazhishi.zheng.jersey.configuration.JerseyConfiguration;
import com.dadazhishi.zheng.jersey.configuration.ServerConnectorConfiguration;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class JettyAppServer implements AppServer {

  private final JerseyConfiguration jerseyConfiguration;
  private final Supplier<Injector> injectorSupplier;
  private final Server server;
  private final ServletContextHandlerConfigurer servletContextHandlerConfigurer;

  public JettyAppServer(JerseyConfiguration jerseyConfiguration,
      Supplier<Injector> injectorSupplier,
      JettyServerCreator jettyServerCreator,
      ServletContextHandlerConfigurer servletContextHandlerConfigurer) {
    this.jerseyConfiguration = jerseyConfiguration;
    this.injectorSupplier = injectorSupplier;
    this.server = jettyServerCreator.create();
    this.servletContextHandlerConfigurer = servletContextHandlerConfigurer;
    configureServer();
  }

  private void configureServer() {
    List<ServerConnectorConfiguration> serverConnectorConfigurations = jerseyConfiguration
        .getServerConnectors();
    serverConnectorConfigurations.forEach(configuration -> {
      ServerConnector connector = new ServerConnector(server);
      connector.setName(configuration.getName());
      connector.setHost(configuration.getHost());
      connector.setPort(configuration.getPort());
      server.addConnector(connector);
    });
    ServletContextHandler servletContextHandler = new ServletContextHandler(
        ServletContextHandler.SESSIONS);
    servletContextHandler.addServlet(DefaultServlet.class, "/");
    servletContextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    servletContextHandlerConfigurer.configure(servletContextHandler);
    servletContextHandler.setServer(server);

    ServletHolder holder = new ServletHolder(ServletContainer.class);
    holder.setInitParameter("javax.ws.rs.Application", GuiceJerseyResourceConfig.class.getName());

    servletContextHandler.addServlet(holder, "/*");
    servletContextHandler.setContextPath(jerseyConfiguration.getContextPath());
    servletContextHandler.addEventListener(new GuiceServletContextListener() {
      @Override
      protected Injector getInjector() {
        return injectorSupplier.get();
      }
    });
    server.setHandler(servletContextHandler);
  }

  @Override
  public void start() throws Exception {
    server.start();

  }

  @Override
  public void join() throws Exception {
    server.join();
  }

  @Override
  public void stop() throws Exception {
    server.stop();
  }
}
