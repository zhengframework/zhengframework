package com.github.zhengframework.rest;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.github.zhengframework.web.WebConfig;
import com.google.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class RestTest {

  @Inject private WebConfig webConfig;

  @Test
  @WithZhengApplication(moduleClass = {MyModule.class})
  public void start() throws Exception {

    Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://localhost:" + webConfig.getPort()).path("/");
    String response = target.path(TestResource.PATH).request().get().readEntity(String.class);
    log.info("{}", response);
    Assert.assertEquals(TestResource.MESSAGE, response);
  }

  @Test
  @WithZhengApplication(moduleClass = {MyModule.class})
  public void testInject() throws Exception {
    Client client = ClientBuilder.newClient();
    WebTarget target =
        client.target("http://localhost:" + webConfig.getPort()).path(webConfig.getContextPath());
    String response1 =
        target.path(TestResource.PATH + "/inject").request().get().readEntity(String.class);
    log.info("{}", response1);
    String response2 =
        target.path(TestResource.PATH + "/inject").request().get().readEntity(String.class);
    log.info("{}", response2);
    Assert.assertNotEquals(response1, response2);
  }
}
