package com.github.zhengframework.mybatis;

/*-
 * #%L
 * zheng-mybatis
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
import com.github.zhengframework.guice.ExposedPrivateModule;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.TypeHandler;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@ToString
@EqualsAndHashCode
@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.mybatis", supportGroup = true)
public class MyBatisConfig implements ConfigurationDefine {

  private String environmentId;
  private boolean lazyLoadingEnabled = false;
  private boolean aggressiveLazyLoading = true;
  private boolean multipleResultSetsEnabled = true;
  private boolean useColumnLabel = true;
  private boolean cacheEnabled = true;
  private boolean useGeneratedKeys = false;
  private Integer defaultStatementTimeout;
  private ExecutorType executorType = ExecutorType.SIMPLE;
  private AutoMappingBehavior autoMappingBehavior = AutoMappingBehavior.PARTIAL;
  private boolean failFast = false;
  private String mapperClassPackages;
  private Collection<Class<?>> mapperClasses = new HashSet<>();
  private Collection<Class<? extends Interceptor>> interceptorsClasses = new HashSet<>();
  private Collection<Class<? extends TypeHandler<?>>> handlersClasses = new HashSet<>();
  private LocalCacheScope localeCacheScope;
  private Boolean mapUnderscoreToCamelCase;
  private Class<? extends ExposedPrivateModule> extraModuleClass;
  private String configFile;
  @JsonIgnore private Map<String, String> properties = new HashMap<>();
}
