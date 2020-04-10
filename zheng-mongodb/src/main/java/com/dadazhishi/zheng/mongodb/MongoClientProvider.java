package com.github.zhengframework.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class MongoClientProvider implements Provider<MongoClient> {

  private final MongoConfig mongoConfig;

  @Inject
  public MongoClientProvider(MongoConfig mongoConfig) {
    this.mongoConfig = mongoConfig;
  }

  @Override
  public MongoClient get() {
    ConnectionString connectionString = new ConnectionString(mongoConfig.getUrl());
    return MongoClients.create(connectionString);
  }
}
