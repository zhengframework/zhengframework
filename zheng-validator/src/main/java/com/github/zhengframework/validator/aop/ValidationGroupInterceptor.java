package com.github.zhengframework.validator.aop;

/*-
 * #%L
 * zheng-validator
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
