package com.github.zhengframework.jdbc.ebean;

import com.google.inject.Provider;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import java.util.Optional;
import javax.inject.Singleton;
import javax.sql.DataSource;

@Singleton
public class DataBaseProvider implements Provider<Database> {

  private final EbeanConfig ebeanConfig;
  private final Provider<DataSource> dataSourceProvider;
  private final Provider<DatabaseConfigConfigurer> databaseConfigConfigurerProvider;

  public DataBaseProvider(EbeanConfig ebeanConfig, Provider<DataSource> dataSourceProvider,
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
    Optional.ofNullable(ebeanConfig.getDatabasePlatformName()).ifPresent(
        config::setDatabasePlatformName);
    config.setMappingLocations(ebeanConfig.getMappingLocations());
    config.setIdGeneratorAutomatic(ebeanConfig.isIdGeneratorAutomatic());
    databaseConfigConfigurerProvider.get().configure(config);
    return DatabaseFactory.create(config);
  }
}
