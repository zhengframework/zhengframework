package com.github.zhengframework.mybatis;

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
      PackageScanMapperClassProvider managedClassProvider = new PackageScanMapperClassProvider(
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
