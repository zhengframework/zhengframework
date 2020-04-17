//package com.github.zhengframework.configuration.parser;
//
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//
//public abstract class GenericType<T> {
//
//  private final Type type;
//
//  public GenericType() {
//    if (GenericType.class != getClass().getSuperclass()) {
//      String errorMsg = "'%s' must be a direct subclass of '%s'";
//      errorMsg = String.format(errorMsg, getClass().getName(), GenericType.class.getName());
//      throw new IllegalArgumentException(errorMsg);
//    }
//    Type t = getClass().getGenericSuperclass();
//    if (t instanceof ParameterizedType) {
//      ParameterizedType superClass = (ParameterizedType) t;
//      type = superClass.getActualTypeArguments()[0];
//    } else {
//      String message = "'%s' must be parameterized (for example \"new GenericType<List<Integer>>(){}\"), "
//          + "it cannot be of raw type \"new GenericType(){}\".";
//      throw new IllegalStateException(String.format(message, getClass().getName()));
//    }
//  }
//
//  public final Type getType() {
//    return type;
//  }
//
//  @Override
//  final public String toString() {
//    return type.toString();
//  }
//
//}
