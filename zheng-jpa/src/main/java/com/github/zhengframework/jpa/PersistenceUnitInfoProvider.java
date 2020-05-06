package com.github.zhengframework.jpa;

/*-
 * #%L
 * zheng-jpa
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;

public class PersistenceUnitInfoProvider implements Provider<PersistenceUnitInfo> {

  private final JpaConfig jpaConfig;

  @Inject
  public PersistenceUnitInfoProvider(JpaConfig jpaConfig) {
    this.jpaConfig = jpaConfig;
  }

  @Override
  public PersistenceUnitInfo get() {
    PersistenceUnitInfoImpl persistenceUnitInfo = new PersistenceUnitInfoImpl();
    persistenceUnitInfo.setClassLoader(Thread.currentThread().getContextClassLoader());
    if (jpaConfig.getPersistenceUnitName() == null) {
      persistenceUnitInfo.setPersistenceUnitName("");
    } else {
      persistenceUnitInfo.setPersistenceUnitName(jpaConfig.getPersistenceUnitName());
    }
    Properties properties = configToProperties(jpaConfig);
    persistenceUnitInfo.setProperties(properties);
    persistenceUnitInfo.setTransactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL);

    List<String> managedClassNames = new ArrayList<>();
    if (jpaConfig.getManagedClasses() != null) {
      managedClassNames.addAll(jpaConfig.getManagedClasses());
    }
    String managedClassPackages = jpaConfig.getManagedClassPackages();
    if (managedClassPackages != null && !managedClassPackages.isEmpty()) {
      String[] strings = managedClassPackages.trim().split(",");
      PackageScanManagedClassProvider managedClassProvider =
          new PackageScanManagedClassProvider(
              Arrays.stream(strings).map(String::trim).distinct().toArray(String[]::new));
      List<String> stringList = managedClassProvider.get();
      if (stringList != null) {
        managedClassNames.addAll(stringList);
      }
    }
    persistenceUnitInfo.setManagedClassNames(managedClassNames);
    return persistenceUnitInfo;
  }

  private Properties configToProperties(JpaConfig persistenceConfig) {
    Properties properties = new Properties();
    if (persistenceConfig.getDriverClassName() != null) {
      properties.put("javax.persistence.jdbc.driver", persistenceConfig.getDriverClassName());
    }
    if (persistenceConfig.getUrl() != null) {
      properties.put("javax.persistence.jdbc.url", persistenceConfig.getUrl());
    }
    if (persistenceConfig.getUsername() != null) {
      properties.put("javax.persistence.jdbc.user", persistenceConfig.getUsername());
    }
    if (persistenceConfig.getPassword() != null) {
      properties.put("javax.persistence.jdbc.password", persistenceConfig.getPassword());
    } else {
      properties.put("javax.persistence.jdbc.password", "");
    }
    if (persistenceConfig.getProperties() != null && !persistenceConfig.getProperties().isEmpty()) {
      persistenceConfig.getProperties().forEach(properties::put);
    }
    return properties;
  }
}
