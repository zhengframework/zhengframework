package com.dadazhishi.zheng.activemq.artemis;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.jms.ConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class ActiveMQArtemisModule extends AbstractModule {

  private String brokerURL;
  private String userName;
  private String password;

  public ActiveMQArtemisModule(String brokerURL) {
    this(brokerURL, null, null);
  }

  public ActiveMQArtemisModule(String brokerURL, String userName, String password) {
    Objects.requireNonNull(brokerURL);
    this.brokerURL = brokerURL;
    this.userName = userName;
    this.password = password;
  }

  @Inject(optional = true)
  @Provides
  public ConnectionFactory connectionFactory(
      @Nullable ActiveMQConnectionConfig activeMQConnectionConfig) {
    try {
      ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
      if (userName != null) {
        connectionFactory.setUser(userName);
      }
      if (password != null) {
        connectionFactory.setPassword(password);
      }
      if (activeMQConnectionConfig != null) {
        activeMQConnectionConfig.config(connectionFactory);
      }
      return connectionFactory;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  protected void configure() {
    ActiveMQConnectionConfig activeMQConnectionConfig = connectionFactory -> {
    };
    OptionalBinder.newOptionalBinder(binder(), ActiveMQConnectionConfig.class)
        .setDefault()
        .toInstance(activeMQConnectionConfig)
    ;
  }

  public static final class ActiveMQArtemisBuilder {

    private String brokerURL;
    private String userName;
    private String password;

    private ActiveMQArtemisBuilder() {
    }

    public static ActiveMQArtemisBuilder builder() {
      return new ActiveMQArtemisBuilder();
    }

    public ActiveMQArtemisBuilder withBrokerURL(String brokerURL) {
      this.brokerURL = brokerURL;
      return this;
    }

    public ActiveMQArtemisBuilder withUserName(String userName) {
      this.userName = userName;
      return this;
    }

    public ActiveMQArtemisBuilder withPassword(String password) {
      this.password = password;
      return this;
    }


    public ActiveMQArtemisModule build() {
      return new ActiveMQArtemisModule(brokerURL, userName, password);
    }
  }
}
