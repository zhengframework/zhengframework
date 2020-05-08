package com.github.zhengframework.jdbc.ebean;

/*-
 * #%L
 * zheng-jdbc-ebean
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

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import java.util.Optional;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;

@Singleton
public class DataBaseProvider implements Provider<Database> {

  private final EbeanConfig ebeanConfig;
  private final Provider<DataSource> dataSourceProvider;
  private final Provider<DatabaseConfigConfigurer> databaseConfigConfigurerProvider;

  public DataBaseProvider(
      EbeanConfig ebeanConfig,
      Provider<DataSource> dataSourceProvider,
      Provider<DatabaseConfigConfigurer> databaseConfigConfigurerProvider) {
    this.ebeanConfig = ebeanConfig;
    this.dataSourceProvider = dataSourceProvider;
    this.databaseConfigConfigurerProvider = databaseConfigConfigurerProvider;
  }

  @Override
  public Database get() {
    DatabaseConfig config = new DatabaseConfig();
    config.setDataSource(dataSourceProvider.get());
    config.setName(ebeanConfig.getName());
    config.setDdlGenerate(ebeanConfig.isDdlGenerate());
    config.setDdlRun(ebeanConfig.isDdlRun());
    config.setDdlCreateOnly(ebeanConfig.isDdlCreateOnly());
    config.setDdlExtra(ebeanConfig.isDdlExtra());
    config.setRegister(ebeanConfig.isRegister());
    config.setDefaultServer(ebeanConfig.isDefaultServer());
    config.setClasses(ebeanConfig.getClasses());
    config.setPackages(ebeanConfig.getPackages());
    config.setDefaultEnumType(ebeanConfig.getDefaultEnumType());
    Optional.ofNullable(ebeanConfig.getDatabasePlatformName())
        .ifPresent(config::setDatabasePlatformName);
    config.setMappingLocations(ebeanConfig.getMappingLocations());
    config.setIdGeneratorAutomatic(ebeanConfig.isIdGeneratorAutomatic());
    databaseConfigConfigurerProvider.get().configure(config);
    return DatabaseFactory.create(config);
  }
}
