package com.github.zhengframework.jpa.hibernate;

/*-
 * #%L
 * zheng-jpa-hibernate
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

import com.github.zhengframework.jpa.EntityManagerFactoryProvider;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class HibernateEntityManagerFactoryProvider implements EntityManagerFactoryProvider {

  @Override
  public EntityManagerFactory get(PersistenceUnitInfo persistenceUnitInfo) {
    final BootstrapServiceRegistryBuilder builder = new BootstrapServiceRegistryBuilder();
    BootstrapServiceRegistry bootstrapServiceRegistry = builder.build();
    Configuration configuration = new Configuration();
    Properties properties = persistenceUnitInfo.getProperties();
    if (properties.getProperty("javax.persistence.jdbc.driver") != null) {
      properties.put(
          "hibernate.connection.driver_class",
          properties.getProperty("javax.persistence.jdbc.driver"));
    }
    if (properties.getProperty("javax.persistence.jdbc.url") != null) {
      properties.put(
          "hibernate.connection.url", properties.getProperty("javax.persistence.jdbc.url"));
    }
    if (properties.getProperty("javax.persistence.jdbc.user") != null) {
      properties.put(
          "hibernate.connection.username", properties.getProperty("javax.persistence.jdbc.user"));
    }
    if (properties.getProperty("javax.persistence.jdbc.password") != null) {
      properties.put(
          "hibernate.connection.password",
          properties.getProperty("javax.persistence.jdbc.password"));
    } else {
      properties.put("hibernate.connection.password", "");
    }

    configuration.addProperties(properties);
    for (String managedClassName : persistenceUnitInfo.getManagedClassNames()) {
      try {
        configuration.addAnnotatedClass(Class.forName(managedClassName));
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    ServiceRegistry registry =
        new StandardServiceRegistryBuilder(bootstrapServiceRegistry)
            .applySettings(configuration.getProperties())
            .build();
    return configuration.buildSessionFactory(registry);
  }
}
