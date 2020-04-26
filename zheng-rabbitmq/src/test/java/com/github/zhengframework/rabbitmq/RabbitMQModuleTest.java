package com.github.zhengframework.rabbitmq;

import static org.junit.Assert.assertTrue;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Injector;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMq;
import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMqConfig;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class RabbitMQModuleTest {

  EmbeddedRabbitMq rabbitMq;
  EmbeddedRabbitMqConfig config;
  @Inject
  private Injector injector;

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
        .port(25599)
        .useCachedDownload(true)
        .deleteDownloadedFileOnErrors(true)
        .downloadReadTimeoutInMillis(10000000)
        .defaultRabbitMqCtlTimeoutInMillis(1000000)
        .downloadConnectionTimeoutInMillis(1000000)
        .rabbitMqServerInitializationTimeoutInMillis(1000000)
        .build();

    rabbitMq = new EmbeddedRabbitMq(config);
    rabbitMq.start();
  }


  @After
  public void close() {
    rabbitMq.stop();
  }

  @Test
  @WithZhengApplication
  public void connectionFactory() throws IOException, TimeoutException {
    Connection connection = injector.getInstance(Connection.class);
    assertTrue(connection.isOpen());
    Channel channel = connection.createChannel();
    assertTrue(channel.isOpen());
    channel.close();
    connection.close();
  }
}