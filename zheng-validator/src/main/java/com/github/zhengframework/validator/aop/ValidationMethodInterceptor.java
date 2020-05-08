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

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ValidationMethodInterceptor implements MethodInterceptor {

  private final Provider<Validator> validatorProvider;

  @Inject private final Provider<ValidationContext> validationContext;

  @Inject
  public ValidationMethodInterceptor(
      Provider<Validator> validatorProvider, Provider<ValidationContext> validationContext) {
    this.validatorProvider = validatorProvider;
    this.validationContext = validationContext;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    ValidationContext context = this.validationContext.get();
    final Class<?>[] groups = context.getContextGroups();
    ExecutableValidator executableValidator = validatorProvider.get().forExecutables();
    Set<ConstraintViolation<Object>> violations =
        executableValidator.validateParameters(
            invocation.getThis(), invocation.getMethod(), invocation.getArguments(), groups);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(
          getMessage(invocation.getMethod(), invocation.getArguments(), violations), violations);
    }

    Object result = invocation.proceed();

    violations =
        executableValidator.validateReturnValue(
            invocation.getThis(), invocation.getMethod(), result, groups);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(
          getMessage(invocation.getMethod(), invocation.getArguments(), violations), violations);
    }
    return result;
  }

  private String getMessage(
      Member member, Object[] args, Set<? extends ConstraintViolation<?>> violations) {

    StringBuilder message = new StringBuilder();
    message.append(violations.size());
    message.append(" constraint violation(s) occurred during method validation.");
    message.append("\nConstructor or Method: ");
    message.append(member);
    message.append("\nArgument values: ");
    message.append(Arrays.toString(args));
    message.append("\nConstraint violations: ");

    int i = 1;
    for (ConstraintViolation<?> constraintViolation : violations) {
      Path.Node leafNode = getLeafNode(constraintViolation);

      message.append("\n (");
      message.append(i);
      message.append(")");
      message.append(" Kind: ");
      message.append(leafNode.getKind());
      if (leafNode.getKind() == ElementKind.PARAMETER) {
        message.append("\n parameter index: ");
        message.append(leafNode.as(Path.ParameterNode.class).getParameterIndex());
      }
      message.append("\n message: ");
      message.append(constraintViolation.getMessage());
      message.append("\n root bean: ");
      message.append(constraintViolation.getRootBean());
      message.append("\n property path: ");
      message.append(constraintViolation.getPropertyPath());
      message.append("\n constraint: ");
      message.append(constraintViolation.getConstraintDescriptor().getAnnotation());

      i++;
    }

    return message.toString();
  }

  private Path.Node getLeafNode(ConstraintViolation<?> constraintViolation) {
    Iterator<Node> nodes = constraintViolation.getPropertyPath().iterator();
    Path.Node leafNode = null;
    while (nodes.hasNext()) {
      leafNode = nodes.next();
    }
    return leafNode;
  }
}
