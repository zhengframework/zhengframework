package com.github.zhengframework.remoteconfig.servlet;

/*-
 * #%L
 * zheng-remote-config
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

  private transient RemoteConfigServer remoteConfigServer;

  private ObjectMapper objectMapper;

  @Inject
  public RemoteConfigServerServlet(
      RemoteConfigServer remoteConfigServer, ObjectMapper objectMapper) {
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

    log.debug("keys={} configParamList={}", Arrays.toString(names), configParamList);

    Map<String, RemoteConfigResponse<?>> responseMap =
        remoteConfigServer.getConfig(names, configParamList);
    objectMapper.writeValue(resp.getOutputStream(), responseMap);
  }
}
