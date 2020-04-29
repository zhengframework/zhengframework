package com.github.zhengframework.validator.aop;

import com.github.zhengframework.validator.ValidationGroups;
import com.github.zhengframework.validator.group.MethodGroupsFactory;
import com.google.inject.Provider;
import javax.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Intercepts methods under {@link ValidationGroups} annotation to declare validation groups in
 * scope of annotated method call.
 *
 * @author Vyacheslav Rusakov
 * @see ValidationContext
 * @since 07.03.2016
 */
public class ValidationGroupInterceptor implements MethodInterceptor {

  private final Provider<ValidationContext> validationContextProvider;

  private final Provider<MethodGroupsFactory> methodGroupsFactoryProvider;

  @Inject
  public ValidationGroupInterceptor(
      Provider<ValidationContext> validationContextProvider,
      Provider<MethodGroupsFactory> methodGroupsFactoryProvider) {
    this.validationContextProvider = validationContextProvider;
    this.methodGroupsFactoryProvider = methodGroupsFactoryProvider;
  }

  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {
    MethodGroupsFactory factory = methodGroupsFactoryProvider.get();
    ValidationContext context = validationContextProvider.get();
    final Class<?>[] groups = factory.create(invocation.getMethod());
    return context.doWithGroups(invocation::proceed, groups);
  }
}
