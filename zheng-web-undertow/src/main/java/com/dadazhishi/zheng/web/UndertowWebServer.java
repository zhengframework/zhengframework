package com.dadazhishi.zheng.web;

import static io.undertow.servlet.Servlets.deployment;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.GuiceFilter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.ClassIntrospecter;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ListenerInfo;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import java.util.Optional;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import javax.websocket.server.ServerEndpointConfig;
import org.xnio.OptionMap;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

@Singleton
public class UndertowWebServer implements WebServer {

  ClassIntrospecter classIntrospecter;
  WebSocketEndpoint annotatedEndpointClass;
  ServerEndpointConfig programmaticEndpointConfig;
  ServerEndpointConfig.Configurator defaultServerEndpointConfigurator;
  private WebConfig webConfig;
  private EventListenerClassScanner eventListenerScanner;
  private Undertow server;

  @Override
  public void init(Provider<WebConfig> webConfigProvider, Provider<Injector> injectorProvider) {
    this.webConfig = webConfigProvider.get();
    Injector injector = injectorProvider.get();
    this.eventListenerScanner = new EventListenerClassScanner(injector);
    classIntrospecter = injector.getInstance(ClassIntrospecter.class);
    annotatedEndpointClass = injector
        .getInstance(Key.get(new TypeLiteral<Optional<WebSocketEndpoint>>() {
        })).orElse(null);
    programmaticEndpointConfig = injector
        .getInstance(Key.get(new TypeLiteral<Optional<ServerEndpointConfig>>() {
        })).orElse(null);
    defaultServerEndpointConfigurator = injector
        .getInstance(ServerEndpointConfig.Configurator.class);
  }

  /**
   * Start the web server. Does not block.
   */
  public void start() throws Exception {
    Preconditions.checkState(server == null, "Server already started");

    DeploymentInfo deploymentInfo = Servlets.deployment()
        .setClassLoader(UndertowWebServer.class.getClassLoader())
        .setContextPath(webConfig.getContextPath())
        .setDeploymentName("zheng-web-undertow-" + webConfig.getPort() + ".war");

    deploymentInfo.addFilter(new FilterInfo("GuiceFilter", GuiceFilter.class)
        .setAsyncSupported(true));
    deploymentInfo.addFilterUrlMapping("GuiceFilter", "/*", DispatcherType.REQUEST);

    eventListenerScanner
        .accept(thing -> deploymentInfo.addListener(new ListenerInfo(thing.getClass(),
            new ImmediateInstanceFactory<>(thing))));

    DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
    manager.deploy();
    PathHandler path = Handlers.path(Handlers.redirect(webConfig.getContextPath()))
        .addPrefixPath(webConfig.getContextPath(), manager.start());

    server = Undertow.builder()
        .addHttpListener(webConfig.getPort(), "localhost")
        .setHandler(path)
        .build();

    Xnio nio = Xnio.getInstance("nio", Undertow.class.getClassLoader());
    XnioWorker worker = nio.createWorker(OptionMap.builder().getMap());
    DefaultByteBufferPool buffers = new DefaultByteBufferPool(true, 1024 * 16, -1, 4);

    if (annotatedEndpointClass != null) {
      WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo()
          .addEndpoint(annotatedEndpointClass.getClass())
          .setWorker(worker)
          .setDispatchToWorkerThread(true)
          .setBuffers(buffers);
      DeploymentInfo websocketDeployment = deployment()
          .setClassIntrospecter(classIntrospecter)
          .setContextPath("/")
          .setSecurityDisabled(true)
          .setAuthorizationManager(null)
//          .setSessionManagerFactory(new InMemorySessionManagerFactory())
          .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
              webSocketDeploymentInfo)
          .setDeploymentName("websocket-annotated-deployment")
          .setClassLoader(annotatedEndpointClass.getClass().getClassLoader());
      DeploymentManager websocketManager = Servlets.defaultContainer()
          .addDeployment(websocketDeployment);
      websocketManager.deploy();
    }

    if (programmaticEndpointConfig != null) {
      WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo()
          .addEndpoint(programmaticEndpointConfig)
          .setWorker(worker)
          .setDispatchToWorkerThread(true)
          .setBuffers(buffers);

      DeploymentInfo websocketDeployment = deployment()
          .setClassIntrospecter(classIntrospecter)
          .setContextPath("/")
          .setSecurityDisabled(true)
          .setAuthorizationManager(null)
//          .setSessionManagerFactory(new InMemorySessionManagerFactory())
          .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
              webSocketDeploymentInfo)
          .setDeploymentName("websocket-programmatic-deployment")
          .setClassLoader(programmaticEndpointConfig.getClass().getClassLoader());

      DeploymentManager websocketManager = Servlets.defaultContainer()
          .addDeployment(websocketDeployment);
      websocketManager.deploy();
    }

    // Start the server
    server.start();
  }

  /**
   * signal the web server to stop
   */
  public void stop() throws Exception {
    Preconditions.checkState(server != null, "Server not started");
    server.stop();
  }
}
