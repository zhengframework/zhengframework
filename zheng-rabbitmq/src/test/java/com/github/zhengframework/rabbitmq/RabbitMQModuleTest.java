package com.github.zhengframework.rabbitmq;

import static org.junit.Assert.assertTrue;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.MapConfiguration;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMq;
import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMqConfig;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RabbitMQModuleTest {


  EmbeddedRabbitMq rabbitMq;
  EmbeddedRabbitMqConfig config;

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Before
  public void setup() throws IOException {
    String home = System.getProperty("user.home");
    File dir = new File(home, ".rabbitmq-embedded/");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File download = new File(dir, "download");
    if (!download.exists()) {
      download.mkdirs();
    }
    File extract = new File(dir, "extract");
    if (!extract.exists()) {
      extract.mkdirs();
    }
    config = new EmbeddedRabbitMqConfig.Builder()
        .downloadFolder(download)
        .extractionFolder(extract)
        .randomPort()
        .useCachedDownload(true)
        .deleteDownloadedFileOnErrors(true)
        .downloadReadTimeoutInMillis(10000000)
        .defaultRabbitMqCtlTimeoutInMillis(1000000)
        .downloadConnectionTimeoutInMillis(1000000)
        .rabbitMqServerInitializationTimeoutInMillis(1000000)
        .randomPort()
        .build();

    rabbitMq = new EmbeddedRabbitMq(config);
    rabbitMq.start();
  }


  @After
  public void close() {
    rabbitMq.stop();
  }

  @Test
  public void connectionFactory() throws IOException, TimeoutException {
    Map<String, String> map = new HashMap<>();
    map.put("zheng.rabbitmq.host", "localhost");
    map.put("zheng.rabbitmq.port", "" + config.getRabbitMqPort());
    System.out.println(map);
    MapConfiguration configuration = new MapConfiguration(map);

    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .withConfiguration(configuration)
        .build();

    Connection connection = application.getInjector().getInstance(Connection.class);
    assertTrue(connection.isOpen());
    Channel channel = connection.createChannel();
    assertTrue(channel.isOpen());
    channel.close();
    connection.close();
  }
}