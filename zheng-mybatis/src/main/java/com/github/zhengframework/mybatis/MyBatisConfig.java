package com.github.zhengframework.mybatis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import com.github.zhengframework.guice.ExposedPrivateModule;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.TypeHandler;

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
  @JsonIgnore
  private Map<String, String> properties = new HashMap<>();
}
