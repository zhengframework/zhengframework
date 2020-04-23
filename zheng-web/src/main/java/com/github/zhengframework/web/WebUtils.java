package com.github.zhengframework.web;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Objects;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class WebUtils {

  private WebUtils() {
  }

  public static SSLContext createSSLContext(final KeyStore keyStore, final KeyStore trustStore,
      String password) throws Exception {
    KeyManager[] keyManagers;
    KeyManagerFactory keyManagerFactory = KeyManagerFactory
        .getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, Objects.requireNonNull(password).toCharArray());
    keyManagers = keyManagerFactory.getKeyManagers();

    TrustManager[] trustManagers;
    TrustManagerFactory trustManagerFactory = TrustManagerFactory
        .getInstance(KeyManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore);
    trustManagers = trustManagerFactory.getTrustManagers();

    SSLContext sslContext;
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagers, trustManagers, null);

    return sslContext;
  }

  public static KeyStore loadKeyStore(String type, String file, String password) throws Exception {
    try (InputStream is = Files.newInputStream(Paths.get(file))) {
      KeyStore loadedKeystore = KeyStore.getInstance(type);
      loadedKeystore.load(is, password.toCharArray());
      return loadedKeystore;
    }
  }
}
