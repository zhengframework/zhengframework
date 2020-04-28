package com.github.zhengframework.validator;

import com.google.inject.Provider;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.MethodType;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public final class ValidationMethodInterceptor implements MethodInterceptor {

  private static final Class<?>[] CAUSE_TYPES = new Class[]{Throwable.class};
  private static final Class<?>[] MESSAGE_CAUSE_TYPES = new Class[]{String.class, Throwable.class};
  private final Provider<Validator> validatorProvider;

  @Inject
  public ValidationMethodInterceptor(Provider<Validator> validatorProvider) {
    this.validatorProvider = validatorProvider;
  }

  private static Throwable getException(ConstraintViolationException exception,
      Class<? extends Throwable> exceptionWrapperClass,
      String exceptionMessage,
      Object[] arguments) {
    // check the thrown exception is of same re-throw type
    if (exceptionWrapperClass == ConstraintViolationException.class) {
      return exception;
    }

//    log.info("error", exception);
    // re-throw the exception as new exception
    Throwable rethrowEx = null;

    String errorMessage;
    Object[] initargs;
    Class<?>[] initargsType;

    if (exceptionMessage.length() != 0) {
      errorMessage = String.format(exceptionMessage, arguments);
      initargs = new Object[]{errorMessage, exception};
      initargsType = MESSAGE_CAUSE_TYPES;
    } else {
      initargs = new Object[]{exception};
      initargsType = CAUSE_TYPES;
    }

    Constructor<? extends Throwable> exceptionConstructor = getMatchingConstructor(
        exceptionWrapperClass, initargsType);
    if (exceptionConstructor != null) {
      try {
        rethrowEx = exceptionConstructor.newInstance(initargs);
      } catch (Exception e) {
        errorMessage = String
            .format("Impossible to re-throw '%s', it needs the constructor with %s argument(s).",
                exceptionWrapperClass.getName(),
                Arrays.toString(initargsType));
        rethrowEx = new RuntimeException(errorMessage, e);
      }
    } else {
      errorMessage = String.format(
          "Impossible to re-throw '%s', it needs the constructor with %s or %s argument(s).",
          exceptionWrapperClass.getName(),
          Arrays.toString(CAUSE_TYPES),
          Arrays.toString(MESSAGE_CAUSE_TYPES));
      rethrowEx = new RuntimeException(errorMessage);
    }
//    log.info("rethrowEx={}", rethrowEx.getClass());
    return rethrowEx;
  }

  @SuppressWarnings("unchecked")
  private static <E extends Throwable> Constructor<E> getMatchingConstructor(Class<E> type,
      Class<?>[] argumentsType) {
    Class<? super E> currentType = type;
    while (Object.class != currentType) {
      for (Constructor<?> constructor : currentType.getConstructors()) {
        if (Arrays.equals(argumentsType, constructor.getParameterTypes())) {
          return (Constructor<E>) constructor;
        }
      }
      currentType = currentType.getSuperclass();
    }
    return null;
  }

  // is there any constrained method on this type
// assuming we don't validate on getter execution
  public boolean interceptMethods(Validator validator, Class<?> type) {
    return validator.getConstraintsForClass(type)
        .getConstrainedMethods(MethodType.NON_GETTER).size() > 0;
  }

  // is this method constrained
  public boolean interceptMethod(Validator validator, Class<?> type, Method method) {
    BeanDescriptor bean = validator.getConstraintsForClass(type);
    MethodDescriptor methodDescriptor = bean.getConstraintsForMethod(
        method.getName(), method.getParameterTypes());
    return methodDescriptor != null;
  }

  // should method parameters be validated
  public boolean requiresParametersValidation(Validator validator, Class<?> type, Method method) {
    BeanDescriptor bean = validator.getConstraintsForClass(type);
    MethodDescriptor methodDescriptor = bean.getConstraintsForMethod(
        method.getName(), method.getParameterTypes());
    if (methodDescriptor != null) {
      return methodDescriptor.hasConstrainedParameters();
    } else {
      return false;
    }
  }

  // should method return value be validated?
  public boolean requiresReturnValueValidation(Validator validator, Class<?> type, Method method) {
    BeanDescriptor bean = validator.getConstraintsForClass(type);
    MethodDescriptor methodDescriptor = bean.getConstraintsForMethod(
        method.getName(), method.getParameterTypes());
    if (methodDescriptor != null) {
      return methodDescriptor.hasConstrainedReturnValue();
    } else {
      return false;
    }
  }

  // is there any constrained constructor on this type
  public <T> boolean interceptConstructors(Validator validator, Class<T> type) {
    BeanDescriptor bean = validator.getConstraintsForClass(type);
    return bean.getConstrainedConstructors().size() > 0;
  }

  // is this constructor constrained
  public <T> boolean interceptConstructor(Validator validator, Class<T> type, Constructor<T> ctor) {
    BeanDescriptor bean = validator.getConstraintsForClass(type);
    ConstructorDescriptor constructorDescriptor = bean.getConstraintsForConstructor(
        ctor.getParameterTypes());
    return constructorDescriptor != null;
  }

  // should constructor parameters be validated
  public <T> boolean requiresParametersValidation(Validator validator, Class<T> type,
      Constructor<T> ctor) {
    BeanDescriptor bean = validator.getConstraintsForClass(type);
    ConstructorDescriptor constructorDescriptor = bean.getConstraintsForConstructor(
        ctor.getParameterTypes());
    if (constructorDescriptor != null) {
      return constructorDescriptor.hasConstrainedParameters();
    } else {
      return false;
    }
  }

  public Object invoke(MethodInvocation invocation) throws Throwable {

    Method method = invocation.getMethod();
    Validate validate = method.getAnnotation(Validate.class);
    Class<?>[] groups = validate.groups();

    Class<?> clazz = method.getDeclaringClass();
    Validator validator = validatorProvider.get();
    log.info("requiresParametersValidation={}",
        requiresParametersValidation(validator, clazz, method));
    if (
        requiresParametersValidation(validator, clazz, method)) {
      Object[] arguments = invocation.getArguments();

      ExecutableValidator executableValidator = validator.forExecutables();
      Set<ConstraintViolation<Object>> constraintViolations = executableValidator
          .validateParameters(invocation.getThis(), method, arguments, validate.groups());
      if (!constraintViolations.isEmpty()) {
        throw getException(new ConstraintViolationException(
                String.format("Validation error when calling method '%s' with arguments %s",
                    method,
                    Arrays.deepToString(arguments)),
                constraintViolations),
            validate.rethrowExceptionsAs(),
            validate.exceptionMessage(),
            arguments);
      }


    }
    Object returnedValue = invocation.proceed();
    log.info("requiresReturnValueValidation={}",
        requiresReturnValueValidation(validator, clazz, method));
    if (validate.validateReturnedValue() && requiresReturnValueValidation(validator, clazz,
        method)) {
      ExecutableValidator executableValidator = validator.forExecutables();
      Set<ConstraintViolation<Object>> constraintViolations = executableValidator
          .validateReturnValue(invocation.getThis(), method, returnedValue,
              validate.groups());
      if (!constraintViolations.isEmpty()) {
//        throw new ConstraintViolationException(
//            String.format("Validation error when calling method '%s' with return value %s",
//                method,
//                returnedValue),
//            constraintViolations);
        Object[] arguments = invocation.getArguments();
        throw getException(new ConstraintViolationException(
                String.format("Method '%s' returned a not valid value %s",
                    method,
                    returnedValue),
                constraintViolations),
            validate.rethrowExceptionsAs(),
            validate.exceptionMessage(),
            arguments);
      }
    }
    return returnedValue;
  }

}
