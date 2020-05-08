package com.github.zhengframework.shiro.jaxrs;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.github.zhengframework.web.WebConfig;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class ShiroRestTest {

  @Inject private Injector injector;

  @Test
  @WithZhengApplication(moduleClass = {MyModule.class})
  public void configure() throws Exception {

    SecurityManager securityManager = injector.getInstance(SecurityManager.class);
    SecurityUtils.setSecurityManager(securityManager);
    WebConfig webConfig = injector.getInstance(WebConfig.class);
    log.info("{}", webConfig);

    OkHttpClient okHttpClient =
        new Builder()
            .cookieJar(
                new CookieJar() {
                  final ArrayList<Cookie> oneCookie = new ArrayList<>(1);

                  @Override
                  public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    oneCookie.addAll(cookies);
                  }

                  @Override
                  public List<Cookie> loadForRequest(HttpUrl url) {
                    return oneCookie;
                  }
                })
            .build();

    FormBody formBody =
        new FormBody.Builder().add("username", "lonestarr").add("password", "vespa").build();
    Request request =
        new Request.Builder()
            .url("http://localhost:" + webConfig.getPort() + "/test/login")
            .post(formBody)
            .build();
    log.info("{}", request);
    Response response1 = okHttpClient.newCall(request).execute();
    String resp = Objects.requireNonNull(response1.body()).string();
    log.info("{}", resp);
    assertEquals("currentUser=lonestarr\n", resp);
    for (Entry<String, List<String>> entry : response1.headers().toMultimap().entrySet()) {
      log.info("{}", entry.getKey());
      log.info("{}", entry.getValue());
    }

    Request request1 =
        new Request.Builder()
            .url("http://localhost:" + webConfig.getPort() + "/test/requiresRoles")
            .get()
            .build();
    ResponseBody body = okHttpClient.newCall(request1).execute().body();

    String respBody = body.string();
    log.info("requiresRoles={}", respBody);
    assertEquals("OK", respBody);
  }
}
