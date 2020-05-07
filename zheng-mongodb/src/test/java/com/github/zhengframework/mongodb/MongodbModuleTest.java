package com.github.zhengframework.mongodb;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class MongodbModuleTest {

  private int port;
  private String bindIp;
  private MongodProcess mongod;
  @Inject private Injector injector;

  @SuppressWarnings("SpellCheckingInspection")
  @Before
  public void setup() throws IOException {
    MongodStarter starter = MongodStarter.getDefaultInstance();

    //    port = FreePortFinder.findFreeLocalPort();
    port = 48761;

    bindIp = "localhost";
    IMongodConfig mongodConfig =
        new MongodConfigBuilder()
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
  @WithZhengApplication
  public void test() {
    MongoClient mongoClient = injector.getInstance(MongoClient.class);
    MongoDatabase database = mongoClient.getDatabase("test");
    MongoCollection<BasicDBObject> testCollection =
        database.getCollection("testCollection", BasicDBObject.class);
    Document document = Document.parse("{\"name\":\"hello world\"}");
    BasicDBObject basicDBObject = new BasicDBObject();
    basicDBObject.append("name", "hello");

    InsertOneResult insertOneResult = testCollection.insertOne(basicDBObject);
    log.info("{}", "getInsertedId=" + insertOneResult.getInsertedId());

    FindIterable<BasicDBObject> basicDBObjects = testCollection.find(basicDBObject);
    MongoCursor<BasicDBObject> iterator = basicDBObjects.iterator();
    while (iterator.hasNext()) {
      BasicDBObject next = iterator.next();
      log.info("{}", "getObjectId=" + next.getObjectId("_id"));
      assertEquals(
          insertOneResult.getInsertedId().asObjectId().getValue().toHexString(),
          next.getObjectId("_id").toString());
    }

    mongoClient.close();
  }
}
