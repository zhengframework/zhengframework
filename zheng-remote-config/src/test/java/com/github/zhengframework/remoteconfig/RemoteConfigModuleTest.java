package com.github.zhengframework.remoteconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class RemoteConfigModuleTest {

  @Inject
  private RemoteConfigServer remoteConfigServer;

  @Inject
  private ObjectMapper objectMapper;

  @Test
  @WithZhengApplication(moduleClass = {MyModule.class})
  public void configure() throws JsonProcessingException {
    remoteConfigServer.init();
    testEcho();
    testThrowable();
    testMultiKeys();
    testNotFound();
    testCustomExceptionMapper();
  }

  private void testCustomExceptionMapper() {
    RemoteConfigRequest request = RemoteConfigRequest.builder()
        .configKeys("CustomThrowableRemoteConfig").build();
    Map<String, RemoteConfigResponse<?>> config =
        remoteConfigServer.getConfig(request);

    RemoteConfigResponse<?> customThrowableRemoteConfig = config.get("CustomThrowableRemoteConfig");
    Assert.assertEquals(
        "CustomRemoteConfigExceptionMapper", customThrowableRemoteConfig.getMessage());
  }

  private void testNotFound() {
    RemoteConfigRequest request = RemoteConfigRequest.builder()
        .configKeys("testNotFound").build();
    Map<String, RemoteConfigResponse<?>> config =
        remoteConfigServer.getConfig(request);
    RemoteConfigResponse<?> testNotFound = config.get("testNotFound");
    Assert.assertFalse(testNotFound.isSuccess());
    Assert.assertEquals(
        String.format(Locale.ENGLISH, "'%s' not found", "testNotFound"), testNotFound.getMessage());
  }

  private void testMultiKeys() {
    RemoteConfigRequest request = RemoteConfigRequest.builder()
        .configKeys("key1", "key2").build();
    Map<String, RemoteConfigResponse<?>> config =
        remoteConfigServer.getConfig(request);
    RemoteConfigResponse<?> key1 = config.get("key1");
    RemoteConfigResponse<?> key2 = config.get("key2");
    Assert.assertTrue(key1.isSuccess());
    Assert.assertTrue(key2.isSuccess());
    Assert.assertEquals(key1, key2);
  }

  private void testThrowable() {
    RemoteConfigRequest request = RemoteConfigRequest.builder()
        .configKeys("throwable").build();
    Map<String, RemoteConfigResponse<?>> config =
        remoteConfigServer.getConfig(request);
    RemoteConfigResponse<?> throwable = config.get("throwable");
    Assert.assertFalse(throwable.isSuccess());
    Assert.assertEquals(ThrowableRemoteConfig.MSG, throwable.getMessage());
  }

  @SuppressWarnings("unchecked")
  private void testEcho() throws JsonProcessingException {
    List<ConfigParam> list = new ArrayList<>();
    list.add(ConfigParam.create("k","k"));
    list.add(ConfigParam.create("k2","k2"));
    RemoteConfigRequest request = RemoteConfigRequest.builder()
        .configKeys("echo").configParams(list).build();
    Map<String, RemoteConfigResponse<?>> config =
        remoteConfigServer.getConfig(request);

    RemoteConfigResponse<List<ConfigParam>> echo =
        (RemoteConfigResponse<List<ConfigParam>>) config.get("echo");
    log.info("echo={}", objectMapper.writeValueAsString(echo));
    Assert.assertTrue(echo.isSuccess());
    Assert.assertEquals(list, echo.getData());
  }
}
