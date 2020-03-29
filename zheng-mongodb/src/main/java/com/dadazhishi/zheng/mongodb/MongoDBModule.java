package com.dadazhishi.zheng.mongodb;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDBModule extends AbstractModule {

  private String uri;

  public MongoDBModule(String uri) {
    this.uri = uri;
  }

  @Override
  protected void configure() {

  }

  @Provides
  public MongoClient mongoClient() {
    return MongoClients.create(uri);
  }

}
