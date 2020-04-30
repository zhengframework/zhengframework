package com.github.zhengframework.validator;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.validator.aop.DeclaredMethodMatcher;
import com.github.zhengframework.validator.aop.ValidatedMethodMatcher;
import com.github.zhengframework.validator.aop.ValidationContext;
import com.github.zhengframework.validator.aop.ValidationGroupInterceptor;
import com.github.zhengframework.validator.aop.ValidationGroupMatcher;
import com.github.zhengframework.validator.aop.ValidationMethodInterceptor;
import com.github.zhengframework.validator.group.MethodGroupsFactory;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.OptionalBinder;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;

/**
 * code base on https://github.com/xvik/guice-validator and https://github.com/traycho-zz/bval-guice
 */
public class ValidatorModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, ValidatorConfig> configMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), ValidatorConfig.class);
    ValidatorConfig validatorConfig = configMap.get("");

    if (validatorConfig.isEnable()) {
      OptionalBinder.newOptionalBinder(binder(),
          ConstraintValidatorFactory.class)
          .setDefault().to(GuiceConstraintValidatorFactory.class);
      OptionalBinder.newOptionalBinder(binder(), MessageInterpolator.class);
      OptionalBinder.newOptionalBinder(binder(), TraversableResolver.class);
      OptionalBinder.newOptionalBinder(binder(), ParameterNameProvider.class);
      OptionalBinder.newOptionalBinder(binder(), ValidatorConfigurationConfigurer.class)
          .setDefault().to(ValidatorConfigurationConfigurerImpl.class);

      bind(Validator.class).toProvider(ValidatorProvider.class);

      bind(ValidationContext.class);

      ValidationGroupInterceptor groupInterceptor = new ValidationGroupInterceptor(
          getProvider(ValidationContext.class), getProvider(MethodGroupsFactory.class));

      bindInterceptor(Matchers.any(), new ValidationGroupMatcher(), groupInterceptor);

      ValidationMethodInterceptor methodInterceptor = new ValidationMethodInterceptor(
          getProvider(Validator.class), getProvider(ValidationContext.class));

      if (validatorConfig.isAnnotationOnly()) {
        Class<? extends Annotation> annotationClass = Objects
            .requireNonNull(validatorConfig.getAnnotationClass());
        // all annotated methods
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(annotationClass), methodInterceptor);
        // all methods in annotated beans (do not search for validation annotations!)
        bindInterceptor(Matchers.annotatedWith(annotationClass), new DeclaredMethodMatcher(),
            methodInterceptor);
      } else {
        bindInterceptor(Matchers.any(), new ValidatedMethodMatcher(), methodInterceptor);
      }

    }

  }

}
