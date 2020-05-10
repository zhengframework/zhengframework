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

import java.util.Arrays;
import java.util.Collection;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class MyBatisInternalModule extends org.mybatis.guice.MyBatisModule {

  private final MyBatisConfig myBatisConfig;

  public MyBatisInternalModule(MyBatisConfig myBatisConfig) {
    this.myBatisConfig = myBatisConfig;
  }

  @Override
  protected void initialize() {
    bind(MyBatisConfig.class).toInstance(myBatisConfig);
    environmentId(myBatisConfig.getEnvironmentId());
    bindTransactionFactoryType(JdbcTransactionFactory.class);
    aggressiveLazyLoading(myBatisConfig.isAggressiveLazyLoading());
    lazyLoadingEnabled(myBatisConfig.isLazyLoadingEnabled());
    multipleResultSetsEnabled(myBatisConfig.isMultipleResultSetsEnabled());
    useCacheEnabled(myBatisConfig.isCacheEnabled());
    useColumnLabel(myBatisConfig.isUseColumnLabel());
    useGeneratedKeys(myBatisConfig.isUseGeneratedKeys());
    executorType(myBatisConfig.getExecutorType());
    autoMappingBehavior(myBatisConfig.getAutoMappingBehavior());
    failFast(myBatisConfig.isFailFast());
    addMapperClasses(myBatisConfig.getMapperClasses());

    String managedClassPackages = myBatisConfig.getMapperClassPackages();
    if (managedClassPackages != null && !managedClassPackages.isEmpty()) {
      String[] strings = managedClassPackages.trim().split(",");
      PackageScanMapperClassProvider managedClassProvider =
          new PackageScanMapperClassProvider(
              Arrays.stream(strings).map(String::trim).distinct().toArray(String[]::new));
      Collection<Class<?>> stringList = managedClassProvider.get();
      if (stringList != null) {
        addMapperClasses(stringList);
      }
    }
    addInterceptorsClasses(myBatisConfig.getInterceptorsClasses());
    addTypeHandlersClasses(myBatisConfig.getHandlersClasses());
    if (myBatisConfig.getDefaultStatementTimeout() != null) {
      defaultStatementTimeout(myBatisConfig.getDefaultStatementTimeout());
    }
    if (myBatisConfig.getLocaleCacheScope() != null) {
      localCacheScope(myBatisConfig.getLocaleCacheScope());
    }
  }
}
