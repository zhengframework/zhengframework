package com.github.zhengframework.guice;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnnotatedFieldTypeListener<T extends Annotation> implements TypeListener {

  private final Class<T> annotationClass;
  private final FieldPostProcessor<T> postProcessor;

  public AnnotatedFieldTypeListener(final Class<T> annotationClass, final FieldPostProcessor<T> postProcessor) {
    this.annotationClass = annotationClass;
    this.postProcessor = postProcessor;
  }

  public <I> void hear(final TypeLiteral<I> type, final TypeEncounter<I> encounter) {
    final Class<? super I> actualType = type.getRawType();
    if (!Utils.isPackageValid(actualType)) {
      return;
    }
    Class<? super I> investigatingType = actualType;
    while (investigatingType != null && !investigatingType.equals(Object.class)) {
      for (final Field field : investigatingType.getDeclaredFields()) {
        if (field.isAnnotationPresent(annotationClass)) {
          encounter.register((InjectionListener<I>) injected -> {
            try {
              field.setAccessible(true);
              postProcessor.process(field.getAnnotation(annotationClass), field, injected);
            } catch (Exception ex) {
              throw new IllegalStateException(
                  String.format("Failed to process annotation %s of field %s of class %s",
                      annotationClass.getSimpleName(), field.getName(),
                      injected.getClass().getSimpleName()), ex);
            }
          });
        }
      }
      investigatingType = investigatingType.getSuperclass();
    }
  }
}
