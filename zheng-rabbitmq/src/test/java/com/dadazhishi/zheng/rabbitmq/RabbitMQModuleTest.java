package com.github.zhengframework.rabbitmq;

import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMq;
import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMqConfig;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RabbitMQModuleTest {

  EmbeddedRabbitMq rabbitMq;
  EmbeddedRabbitMqConfig config;

  @Before
  public void setup() {

    config = new EmbeddedRabbitMqConfig.Builder()
        //.downloadFolder(new File("/Users/ysykzheng/IdeaProjects/zheng/rabbitmq-embedded/"))
        //.extractionFolder(new File("/Users/ysykzheng/IdeaProjects/zheng/rabbitmq-embedded/"))
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
    int port = config.getRabbitMqPort();

//    ConnectionFactory connectionFactory = new ConnectionFactory();
//    connectionFactory.setHost("localhost");
//    connectionFactory.setPort(port);
//    connectionFactory.setVirtualHost("/");
//    connectionFactory.setUsername("guest");
//    connectionFactory.setPassword("guest");

//    ConnectionFactory connectionFactory =
//        Guice.createInjector(new RabbitMQModule("amqp://guest:guest@localhost:" + port + "/%2f"))
//            .getInstance(ConnectionFactory.class);
//    Connection connection = connectionFactory.newConnection();
//    assertTrue(connection.isOpen());
//    Channel channel = connection.createChannel();
//    assertTrue(channel.isOpen());
//    channel.close();
//    connection.close();
  }
}