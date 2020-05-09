package com.github.zhengframework.remoteconfig.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zhengframework.remoteconfig.ConfigParam;
import com.github.zhengframework.remoteconfig.RemoteConfigResponse;
import com.github.zhengframework.remoteconfig.RemoteConfigServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoteConfigServerServlet extends HttpServlet {

  private static final long serialVersionUID = 3063575731127695929L;

  private RemoteConfigServer remoteConfigServer;

  private ObjectMapper objectMapper;

  @Inject
  public RemoteConfigServerServlet(
      RemoteConfigServer remoteConfigServer,
      ObjectMapper objectMapper) {
    this.remoteConfigServer = remoteConfigServer;
    this.objectMapper = objectMapper;
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    remoteConfigServer.init();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String[] configNames = req.getParameterValues("configNames");
    String[] names = Optional.ofNullable(configNames).orElse(new String[0]);

    List<ConfigParam> configParamList = new ArrayList<>();
    Enumeration<String> headerNames = req.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String key = headerNames.nextElement();
      String value = req.getHeader(key);
      configParamList.add(ConfigParam.builder().key(key).value(value).build());
    }

    log.debug("keys={} configParamList={}", Arrays.toString(names),configParamList);

    Map<String, RemoteConfigResponse<?>> responseMap = remoteConfigServer
        .getConfig(names, configParamList);
    objectMapper.writeValue(resp.getOutputStream(), responseMap);
  }
}
