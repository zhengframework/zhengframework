package com.github.zhengframework.mybatis;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;

@Data
@NoArgsConstructor
public class MyBatisConfig {

  private String environmentId;
  private boolean lazyLoadingEnabled = false;
  private boolean aggressiveLazyLoading = true;
  private boolean multipleResultSetsEnabled = true;
  private boolean useColumnLabel = true;
  private boolean cacheEnabled = true;
  private boolean useGeneratedKeys = false;
  private String executorType = ExecutorType.SIMPLE.name();
  private String autoMappingBehavior = AutoMappingBehavior.PARTIAL.name();
  private boolean failFast = false;
}
