package com.github.zhengframework.web.undertow;

/*-
 * #%L
 * zheng-web-undertow
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

import com.github.zhengframework.web.WebConfig;
import com.github.zhengframework.web.WebUtils;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.UndertowOptions;
import java.io.IOException;
import java.security.KeyStore;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.xnio.OptionMap;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

@Slf4j
public class UndertowServerProvider implements Provider<Undertow.Builder> {

  private final WebConfig webConfig;

  @Inject
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
          KeyStore keyStore =
              WebUtils.loadKeyStore(
                  webConfig.getKeyStoreType(),
                  webConfig.getKeyStorePath(),
                  webConfig.getKeyStorePassword());
          KeyStore trustStore =
              WebUtils.loadKeyStore(
                  webConfig.getTrustStoreType(),
                  webConfig.getTrustStorePath(),
                  webConfig.getTrustStorePassword());
          sslContext =
              WebUtils.createSSLContext(keyStore, trustStore, webConfig.getKeyStorePassword());
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
