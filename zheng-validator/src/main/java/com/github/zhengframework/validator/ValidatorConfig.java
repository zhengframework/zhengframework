package com.github.zhengframework.validator;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.lang.annotation.Annotation;
import javax.validation.executable.ValidateOnExecution;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@ConfigurationInfo(prefix = "zheng.validator")
public class ValidatorConfig implements ConfigurationDefine {

  private boolean enable = true;

  private boolean annotationOnly = false;
  private Class<? extends Annotation> annotationClass = ValidateOnExecution.class;

  /**
   * By default, ({@link javax.validation.groups.Default}) group is always added to groups defined
   * with {@link ValidationGroups} annotation.
   */
  private boolean addDefaultGroup = true;

}
