// package com.github.zhengframework.configuration.parser;

/*-
 * #%L
 * zheng-core
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
//
//
// import java.lang.reflect.Type;
// import java.util.Map;
//
// public class TypeParser {
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
// }
