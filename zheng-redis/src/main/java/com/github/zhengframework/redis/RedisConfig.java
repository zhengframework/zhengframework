package com.github.zhengframework.redis;

/*-
 * #%L
 * zheng-redis
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

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
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
@ConfigurationInfo(prefix = "zheng.redis", supportGroup = true)
public class RedisConfig implements ConfigurationDefine {

  private String host = "localhost";
  private String clientName;
  private int port = 6379;
  private int database = 0;
  private String password = null;
  private Boolean verifyPeer;
  private Boolean startTls;
  private Boolean ssl;
  private Long timeout = 6L; // seconds
}
