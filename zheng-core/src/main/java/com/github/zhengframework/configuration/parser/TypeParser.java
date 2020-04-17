//package com.github.zhengframework.configuration.parser;
//
//
//import java.lang.reflect.Type;
//import java.util.Map;
//
//public class TypeParser {
//
//  private final Map<Type, Parser<?>> parsers;
//
//  public TypeParser() {
//    this.parsers = Parsers.copyDefault();
//  }
//
//  public boolean containsType(Type type){
//    return parsers.containsKey(type);
//  }
//
//  public void addParser(Type type, Parser<?> parser) {
//    parsers.put(type, parser);
//  }
//
//  public void removeParser(Type type, Parser<?> parser) {
//    parsers.remove(type, parser);
//  }
//
//  public Parser<?> removeParser(Type type) {
//    return parsers.remove(type);
//  }
//
//  public <T> T parse(String input, Class<T> targetType) {
//    if (input == null) {
//      throw new NullPointerException("input is null");
//    } else if (targetType == null) {
//      throw new NullPointerException("targetType is null");
//    } else {
//      return _parse(input, targetType);
//    }
//  }
//
//  public <T> T parse(String input, GenericType<T> genericType) {
//    if (input == null) {
//      throw new NullPointerException("input is null");
//    } else if (genericType == null) {
//      throw new NullPointerException("genericType is null");
//    } else {
//      return _parse(input, genericType.getType());
//    }
//  }
//
//  public <T> T parseType(String input, Type type) {
//    if (input == null) {
//      throw new NullPointerException("input is null");
//    } else if (type == null) {
//      throw new NullPointerException("type is null");
//    } else {
//      return _parse(input, type);
//    }
//  }
//
//  @SuppressWarnings("unchecked")
//  private <T> T _parse(String input, Type type) {
//    if (parsers.containsKey(type)) {
//      return (T) parsers.get(type).parse(input);
//    } else {
//      throw new IllegalStateException("not support type: " + type);
//    }
//  }
//
//}
