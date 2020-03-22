package com.dadazhishi.zheng.activemq;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQModule extends AbstractModule {

  private String brokerURL;
  private String userName;
  private String password;

  public ActiveMQModule(String brokerURL) {
    this(brokerURL, null, null);
  }

  public ActiveMQModule(String brokerURL, String userName, String password) {
    Objects.requireNonNull(brokerURL);
    this.brokerURL = brokerURL;
    this.userName = userName;
    this.password = password;
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

  @Inject(optional = true)
  @Provides
  public ConnectionFactory connectionFactory(
      @Nullable ActiveMQConnectionConfig activeMQConnectionConfig) {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
        brokerURL);
    if (userName != null) {
      connectionFactory.setUserName(userName);
    }
    if (password != null) {
      connectionFactory.setPassword(password);
    }
    if (activeMQConnectionConfig != null) {
      activeMQConnectionConfig.config(connectionFactory);
    }
    return connectionFactory;
  }

  public static final class ActiveMQModuleBuilder {

    private String brokerURL;
    private String userName;
    private String password;

    private ActiveMQModuleBuilder() {
    }

    public static ActiveMQModuleBuilder builder() {
      return new ActiveMQModuleBuilder();
    }

    public ActiveMQModuleBuilder withBrokerURL(String brokerURL) {
      this.brokerURL = brokerURL;
      return this;
    }

    public ActiveMQModuleBuilder withUserName(String userName) {
      this.userName = userName;
      return this;
    }

    public ActiveMQModuleBuilder withPassword(String password) {
      this.password = password;
      return this;
    }

    public ActiveMQModule build() {
      return new ActiveMQModule(brokerURL, userName, password);
    }
  }
}
