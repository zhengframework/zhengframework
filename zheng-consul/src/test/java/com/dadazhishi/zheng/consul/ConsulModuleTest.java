package com.dadazhishi.zheng.consul;

import static org.junit.Assert.assertEquals;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarter;
import com.pszymczyk.consul.ConsulStarterBuilder;
import java.io.IOException;
import java.util.List;
import me.alexpanov.net.FreePortFinder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConsulModuleTest {

  //  @ClassRule
//  public static final ConsulResource consul = new ConsulResource();
  ConsulStarter consulStarter;
  ConsulProcess consulProcess;
  int port;
  private OkHttpClient client = new OkHttpClient();

  @Before
  public void setUp() throws Exception {
    port = FreePortFinder.findFreeLocalPort();
    consulStarter = ConsulStarterBuilder.consulStarter()
        .withHttpPort(port)
        .build();
    consulProcess = consulStarter.start();

  }

  @After
  public void tearDown() throws Exception {
    consulProcess.close();
  }

  @Test
  public void test() throws IOException {
    Request request = new Request.Builder()
        .url("http://localhost:" + port + "/v1/agent/self")
        .build();
    okhttp3.Response response = client.newCall(request).execute();
    int code = response.code();
    assertEquals(200, code);
    System.out.println(response.body().string());
  }

  @Test
  public void test2() {
    ConsulClient client = new ConsulClient("localhost", port);

// set KV
    byte[] binaryData = new byte[]{1, 2, 3, 4, 5, 6, 7};
    client.setKVBinaryValue("someKey", binaryData);

    client.setKVValue("com.my.app.foo", "foo");
    client.setKVValue("com.my.app.bar", "bar");
    client.setKVValue("com.your.app.foo", "hello");
    client.setKVValue("com.your.app.bar", "world");

// get single KV for key
    Response<GetValue> keyValueResponse = client.getKVValue("com.my.app.foo");
    System.out.println(keyValueResponse.getValue().getKey() + ": " + keyValueResponse.getValue()
        .getDecodedValue()); // prints "com.my.app.foo: foo"

// get list of KVs for key prefix (recursive)
    Response<List<GetValue>> keyValuesResponse = client.getKVValues("com.my");
    keyValuesResponse.getValue().forEach(value -> System.out.println(value.getKey() + ": " + value
        .getDecodedValue())); // prints "com.my.app.foo: foo" and "com.my.app.bar: bar"

//list known datacenters
    Response<List<String>> response = client.getCatalogDatacenters();
    System.out.println("Datacenters: " + response.getValue());


  }
}