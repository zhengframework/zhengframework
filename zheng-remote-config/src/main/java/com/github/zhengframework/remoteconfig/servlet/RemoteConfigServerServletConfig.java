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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@ConfigurationInfo(prefix = RemoteConfigServerServletConfig.ZHENG_REMOTE_CONFIG_SERVLET)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteConfigServerServletConfig implements ConfigurationDefine {

  public static final String ZHENG_REMOTE_CONFIG_SERVLET = "zheng.remoteConfig.servlet";
  private boolean enable = true;

  private String basePath = "/config";
  private String parameterName = "key";
}
