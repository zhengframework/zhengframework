package com.github.zhengframework.jdbc.ebean;

import static com.google.inject.name.Names.named;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.github.zhengframework.jdbc.ebean.query.QCustomer;
import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import io.ebean.DB;
import io.ebean.Database;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class EbeanModuleTest {

  @Inject
  private Injector injector;

  @Test
  @WithZhengApplication(configFile = "application.properties")
  public void configure() throws SQLException {
    DataSource dataSource = injector.getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaData.getDatabaseProductName());
    Database instance = injector.getInstance(Database.class);
    assertNotNull(instance);
    insert_via_server(instance);
    insert_via_model();
    updateRob();
    statelessUpdate();
    findAll();
  }

  @Test
  @WithZhengApplication(configFile = "application_group.properties")
  public void configureGroup() throws SQLException {
    DataSource dataSourceA = injector
        .getInstance(Key.get(DataSource.class, named("a")));
    DatabaseMetaData metaDataA = dataSourceA.getConnection().getMetaData();
    System.out.println(metaDataA.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaDataA.getDatabaseProductName());

    DataSource dataSourceB = injector
        .getInstance(Key.get(DataSource.class, named("b")));
    DatabaseMetaData metaDataB = dataSourceB.getConnection().getMetaData();
    Assert.assertEquals(metaDataB.getDatabaseProductName(), metaDataA.getDatabaseProductName());
    Assert.assertNotEquals(dataSourceA, dataSourceB);
    Database a = injector.getInstance(Key.get(Database.class, named("a")));
    assertNotNull(a);
    Database b = injector.getInstance(Key.get(Database.class, named("b")));
    assertNotNull(b);
    Database aDefault = DB.getDefault();
    assertEquals("a", aDefault.getName());
    assertNotNull(DB.byName("a"));
    assertNotNull(DB.byName("b"));

    insert_via_server(a);
    insert_via_model();
    updateRob();
    statelessUpdate();
    findAll();
  }

  public void insert_via_server(Database database) {
    Customer rob = new Customer("Rob");
    database.save(rob);
    assertNotNull(rob.getId());
  }

  public void updateRob() {

    Customer newBob = new Customer("Bob");
    newBob.save();

    Customer bob = DB.find(Customer.class)
        .where().eq("name", "Bob")
        .findOne();

    bob.setNotes("Doing an update");
    bob.save();
  }

  public void insert_via_model() {

    Customer jim = new Customer("Jim");
    jim.save();
    assertNotNull(jim.getId());
  }

  public void statelessUpdate() {

    Customer newMob = new Customer("Mob");
    newMob.save();

    Customer upd = new Customer();
    upd.setId(newMob.getId());
    upd.setNotes("Update without a fetch");

    upd.update();
  }

  public void findAll() {

    DB.find(Customer.class)
        .findList();

    new QCustomer()
        .id.greaterOrEqualTo(1L)
        .findList();
  }
}