package com.github.zhengframework.web;

/*-
 * #%L
 * zheng-web
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@ToString
@EqualsAndHashCode
@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = WebConfig.ZHENG_WEB)
public class WebConfig implements ConfigurationDefine {

  public static final String ZHENG_WEB = "zheng.web";
  private String contextPath = "/";

  private String webSocketPath = "/ws";

  private int port = 8080;

  private boolean http2 = false;

  private boolean ssl = false;

  private int sslPort = 8443;

  private String keyStoreType;

  private String keyStorePath;

  private String keyStorePassword;

  private String trustStoreType;

  private String trustStorePath;

  private String trustStorePassword;

  @JsonIgnore private Map<String, String> properties = new HashMap<>();
}
