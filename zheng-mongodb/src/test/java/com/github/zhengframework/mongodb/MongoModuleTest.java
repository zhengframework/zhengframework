package com.github.zhengframework.mongodb;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import java.io.IOException;
import me.alexpanov.net.FreePortFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MongoModuleTest {

  private MongodProcess mongod;

  @Before
  public void setup() throws IOException {
    MongodStarter starter = MongodStarter.getDefaultInstance();
    int port = FreePortFinder.findFreeLocalPort();
    String bindIp = "localhost";
    IMongodConfig mongodConfig = new MongodConfigBuilder()
        .version(Version.Main.PRODUCTION)
        .net(new Net(bindIp, port, Network.localhostIsIPv6()))
        .build();
    MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
    mongod = mongodExecutable.start();

  }

  @After
  public void close() {
    mongod.stop();
  }

  @Test
  public void mongoClient() {
//    MongoClient mongoClient = Guice
//        .createInjector(new MongoModule(String.format("mongodb://%s:%d", bindIp, port)))
//        .getInstance(MongoClient.class);
//    MongoDatabase database = mongoClient.getDatabase("test");
//    MongoCollection<BasicDBObject> testCollection = database
//        .getCollection("testCollection", BasicDBObject.class);
//    Document document = Document.parse("{\"name\":\"hello world\"}");
//    BasicDBObject basicDBObject = new BasicDBObject();
//    basicDBObject.append("name", "hello");
//
//    InsertOneResult insertOneResult = testCollection.insertOne(basicDBObject);
//    System.out.println("getInsertedId=" + insertOneResult.getInsertedId());
//
//    FindIterable<BasicDBObject> basicDBObjects = testCollection.find(basicDBObject);
//    MongoCursor<BasicDBObject> iterator = basicDBObjects.iterator();
//    while (iterator.hasNext()) {
//      BasicDBObject next = iterator.next();
//      System.out.println("getObjectId=" + next.getObjectId("_id"));
//      assertEquals(insertOneResult.getInsertedId().asObjectId().getValue().toHexString(),
//          next.getObjectId("_id").toString());
//    }
//
//    mongoClient.close();

  }
}