package com.dadazhishi.zheng.jersey.grizzly2;

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
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.servlet.FilterRegistration;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.utils.Charsets;
import org.glassfish.jersey.internal.guava.ThreadFactoryBuilder;
import org.glassfish.jersey.process.JerseyProcessingUncaughtExceptionHandler;
import org.glassfish.jersey.servlet.ServletContainer;

public class GrizzlyAppServer implements AppServer {

  private final JerseyConfiguration jerseyConfiguration;
  private final Supplier<Injector> injectorSupplier;
  private final HttpServer server;

  public GrizzlyAppServer(
      JerseyConfiguration jerseyConfiguration,
      Supplier<Injector> injectorSupplier,
      GrizzlyServerCreator grizzlyServerCreator) {
    this.jerseyConfiguration = jerseyConfiguration;
    this.injectorSupplier = injectorSupplier;
    server = grizzlyServerCreator.create();
    configureServer();
  }

  private void configureServer() {
    List<ServerConnectorConfiguration> serverConnectorConfigurations = jerseyConfiguration
        .getServerConnectors();
    serverConnectorConfigurations.forEach(configuration -> {
      NetworkListener listener = new NetworkListener("grizzly-" + configuration.getName(),
          configuration.getHost(), configuration.getPort());
      listener.getTransport().getWorkerThreadPoolConfig().setThreadFactory(
          (new ThreadFactoryBuilder()).setNameFormat("grizzly-http-server-%d")
              .setUncaughtExceptionHandler(new JerseyProcessingUncaughtExceptionHandler()).build());
      server.addListener(listener);
    });

    WebappContext context = new WebappContext("GrizzlyContext",
        jerseyConfiguration.getContextPath());

    context.addListener(new GuiceServletContextListener() {
      @Override
      protected Injector getInjector() {
        return injectorSupplier.get();
      }
    });

    FilterRegistration guiceFilter = context.addFilter("GuiceFilter", GuiceFilter.class);
    guiceFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), "/*");
    guiceFilter.setAsyncSupported(true);
    ServletRegistration registration = context.addServlet(ServletContainer.class.getName(),
        ServletContainer.class);
    registration
        .setInitParameter("javax.ws.rs.Application", GuiceJerseyResourceConfig.class.getName());
    registration.addMapping("/*");
    ServerConfiguration config = server.getServerConfiguration();
    config.setPassTraceRequest(true);
    config.setDefaultQueryEncoding(Charsets.UTF8_CHARSET);
    config.setGracefulShutdownSupported(true);
    config.setSendFileEnabled(true);
    context.deploy(server);
  }

  @Override
  public void start() throws Exception {
    server.start();
  }

  @Override
  public void join() throws Exception {
  }

  @Override
  public void stop() throws Exception {
    server.shutdown();
  }
}
