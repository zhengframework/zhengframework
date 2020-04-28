package com.github.zhengframework.validator;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.ConstraintViolationException;

/**
 * Marker for methods which arguments have to be validated.
 *
 * @version $Id$
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Validate {

  /**
   * The groups have to be validated, empty by default.
   *
   * @return the groups have to be validated, empty by default.
   */
  Class<?>[] groups() default {};

  /**
   * Marks if the returned object by the intercepted method execution has to be validated, false by
   * default.
   *
   * @return false by default.
   */
  boolean validateReturnedValue() default false;

  /**
   * The exception re-thrown when an error occurs during the validation.
   *
   * @return the exception re-thrown when an error occurs during the validation.
   */
  Class<? extends Throwable> rethrowExceptionsAs() default ConstraintViolationException.class;

  /**
   * A custom error message when throwing the custom exception.
   *
   * It supports java.util.Formatter place holders, intercepted method arguments will be used as
   * message format arguments.
   *
   * @return a custom error message when throwing the custom exception.
   * @see java.util.Formatter#format(String, Object...)
   */
  String exceptionMessage() default "";

}
