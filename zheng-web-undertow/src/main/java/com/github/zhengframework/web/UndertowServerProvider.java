package com.github.zhengframework.web;

import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.UndertowOptions;
import java.io.IOException;
import java.security.KeyStore;
import javax.inject.Provider;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.xnio.OptionMap;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

@Slf4j
public class UndertowServerProvider implements Provider<Undertow.Builder> {

  private final WebConfig webConfig;

  public UndertowServerProvider(WebConfig webConfig) {
    this.webConfig = webConfig;
  }


  @Override
  public Undertow.Builder get() {
    try {
      Xnio nio = Xnio.getInstance("nio", Undertow.class.getClassLoader());
      XnioWorker worker = nio.createWorker(OptionMap.builder().getMap());
      Builder server = Undertow.builder().setWorker(worker);

      SSLContext sslContext;
      if (webConfig.isSsl()) {
        try {
          KeyStore keyStore = WebUtils.loadKeyStore(webConfig.getKeyStoreType(),
              webConfig.getServerKeystore(),
              webConfig.getStorePassword());
          KeyStore trustStore = WebUtils.loadKeyStore(webConfig.getKeyStoreType(),
              webConfig.getServerTruststore(),
              webConfig.getStorePassword());
          sslContext = WebUtils
              .createSSLContext(keyStore, trustStore, webConfig.getStorePassword());
          server.addHttpsListener(webConfig.getSslPort(), "localhost", sslContext);
        } catch (Exception e) {
          log.error("init sslContext fail", e);
        }
      }
      server.addHttpListener(webConfig.getPort(), "localhost");

      if (webConfig.isHttp2()) {
        server.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
        server.setServerOption(UndertowOptions.HTTP2_SETTINGS_ENABLE_PUSH, true);
      }
      return server;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
