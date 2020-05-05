package com.github.zhengframework.jdbc;

/*-
 * #%L
 * zheng-jdbc
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
import com.github.zhengframework.jdbc.wrapper.DataSourceWrapper;
import com.github.zhengframework.jdbc.wrapper.hikari.HikariDataSourceWrapper;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.dataSource", supportGroup = true)
public class DataSourceConfig implements ConfigurationDefine {

  private String username;
  private String password;
  private String driverClassName;
  private String url;
  private Integer loginTimeout;
  private Class<? extends DataSourceWrapper> dataSourceWrapperClass = HikariDataSourceWrapper.class;
  @JsonIgnore
  private Map<String, String> properties = new HashMap<>();
}
