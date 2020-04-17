//package com.github.zhengframework.configuration.parser;
//
//import static java.lang.reflect.Modifier.isStatic;
//
//import com.github.zhengframework.configuration.Configuration;
//import java.beans.BeanInfo;
//import java.beans.Introspector;
//import java.beans.MethodDescriptor;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Array;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Method;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Deque;
//import java.util.EnumSet;
//import java.util.HashSet;
//import java.util.LinkedHashMap;
//import java.util.LinkedHashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.NavigableMap;
//import java.util.Optional;
//import java.util.Queue;
//import java.util.Set;
//import java.util.SortedMap;
//import java.util.SortedSet;
//import java.util.TreeMap;
//import java.util.TreeSet;
//import java.util.concurrent.BlockingDeque;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.ConcurrentSkipListMap;
//import java.util.concurrent.LinkedBlockingDeque;
//
//public class BeanMapper {
//
//
//  public final TypeParser typeParser;
//
//  private final Set<Type> basicTypes;
//
//  public BeanMapper() {
//    typeParser = new TypeParser();
//    basicTypes = getBasicTypes();
//  }
//
//  public void addParser(Type type, Parser<?> parser) {
//    typeParser.addParser(type, parser);
//  }
//
//  public void addBasicParser(Type type, Parser<?> parser) {
//    basicTypes.add(type);
//    typeParser.addParser(type, parser);
//  }
//
//  @SuppressWarnings({"unchecked", "rawtypes"})
//  public <T> T read(Class<T> type, Configuration configuration) throws Exception {
//    if (type.isArray()) {
//      Class componentType = type.getComponentType();
//      TreeMap<String, String> treeMap = new TreeMap<>(String::compareTo);
//      treeMap.putAll(configuration.asMap());
//      Object result = Array.newInstance(componentType, treeMap.size());
//      Collection<String> collection = treeMap.values();
//      int i = 0;
//      for (String value : collection) {
//        Object element = typeParser.parse(value.trim(), componentType);
//        Array.set(result, i, element);
//        //System.out.println("element=="+element);
//        i++;
//      }
//
//      return (T) result;
//    }
//
//    BeanInfo beanInfo = Introspector.getBeanInfo(type);
//    T obj = type.getDeclaredConstructor().newInstance();
//    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//    for (PropertyDescriptor descriptor : propertyDescriptors) {
//      String propertyName = descriptor.getName();
//      Class<?> propertyClass = descriptor.getPropertyType();
//      if (typeParser.containsType(propertyClass)) {
//        if (isBasicType(propertyClass)) {
//          Optional<String> optional = configuration.getString(propertyName);
//          if (optional.isPresent()) {
//            Object o = typeParser.parse(optional.get().trim(), propertyClass);
//            Object[] args = new Object[1];
//            args[0] = o;
//            descriptor.getWriteMethod().invoke(obj, args);
//          }
//        } else {
//          Configuration prefix = configuration.prefix(propertyName);
//          Object o = read(propertyClass, prefix);
//          Object[] args = new Object[1];
//          args[0] = o;
//          descriptor.getWriteMethod().invoke(obj, args);
//        }
//      } else if (EnumSet.class.isAssignableFrom(propertyClass)) {
//        final Type returnType = descriptor.getReadMethod().getGenericReturnType();
//        if (returnType instanceof ParameterizedType) {
//          ParameterizedType parameterizedType = (ParameterizedType) returnType;
//          Type elementType = parameterizedType.getActualTypeArguments()[0];
//          Class<?> elementClass = (Class<?>) elementType;
//          @SuppressWarnings("unchecked")
//          EnumSet collection = EnumSet.noneOf((Class<? extends Enum>) elementClass);
//          List<Configuration> prefixSet = configuration.prefixList(propertyName);
//          for (Configuration configuration1 : prefixSet) {
//            String value = configuration1.asMap().get("");
//            if (value != null) {
//              @SuppressWarnings("unchecked")
//              Object o = Enum.valueOf((Class<? extends Enum>) elementClass, value);
//              //noinspection unchecked
//              collection.add(o);
//            }
//          }
//          Object[] args = new Object[1];
//          args[0] = collection;
//          descriptor.getWriteMethod().invoke(obj, args);
//        }
//      } else if (Collection.class.isAssignableFrom(propertyClass)) {
//        final Type returnType = descriptor.getReadMethod().getGenericReturnType();
//        if (returnType instanceof ParameterizedType) {
//          ParameterizedType parameterizedType = (ParameterizedType) returnType;
//          Type elementType = parameterizedType.getActualTypeArguments()[0];
//          Class<?> elementClass = (Class<?>) elementType;
//          Collection<Object> collection = instantiateCollection(propertyClass);
//          List<Configuration> prefixSet = configuration.prefixList(propertyName);
//          for (Configuration configuration1 : prefixSet) {
//            //System.out.println("configuration1=" + configuration1.asMap());
//            //System.out.println(elementClass);
//            String value = configuration1.getString("", null);
//            if (value != null) {
//              Object o = typeParser.parseType(value, elementType);
//              collection.add(o);
//            } else {
//              Object o = read(elementClass, configuration1);
//              collection.add(o);
//            }
//          }
//          Object[] args = new Object[1];
//          args[0] = collection;
//          descriptor.getWriteMethod().invoke(obj, args);
//        }
//      } else if (Map.class.isAssignableFrom(propertyClass)) {
//        final Type returnType = descriptor.getReadMethod().getGenericReturnType();
//        if (returnType instanceof ParameterizedType) {
//          ParameterizedType parameterizedType = (ParameterizedType) returnType;
//          Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//          Type keyType = actualTypeArguments[0];
//          Type valueType = actualTypeArguments[1];
//          Class keyClass = (Class) keyType;
//          Class valueClass = (Class) valueType;
//          Map<Object, Object> map = instantiateMap(propertyClass);
//          if (typeParser.containsType(keyType)) {
//            if (typeParser.containsType(valueType)) {
//              if (isBasicType(valueClass)) {//判定是否基本类型
//                Configuration prefix = configuration.prefix(propertyName);
//                for (Entry<String, String> entry : prefix.asMap().entrySet()) {
//                  String entryKey = entry.getKey();
//                  String entryValue = entry.getValue();
//                  @SuppressWarnings("unchecked")
//                  Object key = typeParser.parse(entryKey, keyClass);
//                  @SuppressWarnings("unchecked")
//                  Object value = typeParser.parse(entryValue, valueClass);
//                  map.put(key, value);
//                }
//              } else {
//                Map<String, Configuration> prefixMap = configuration
//                    .prefixMap(propertyName);
//                for (Entry<String, Configuration> entry : prefixMap.entrySet()) {
//                  String entryKey = entry.getKey();
//                  @SuppressWarnings("unchecked")
//                  Object key = typeParser.parse(entryKey, keyClass);
//                  Object value;
//                  Configuration entryValue = entry.getValue();
//                  Optional<String> optional = entryValue.getString("");
//                  if (optional.isPresent()) {
//                    value = typeParser.parse(optional.get().trim(), valueClass);
//                  } else {
//                    value = read(valueClass, entryValue);
//                  }
//                  map.put(key, value);
//                }
//              }
//
//            } else {
//              Map<String, Configuration> prefixMap = configuration.prefixMap(propertyName);
//              for (Entry<String, Configuration> entry : prefixMap.entrySet()) {
//                String entryKey = entry.getKey();
//                Configuration entryValue = entry.getValue();
//                @SuppressWarnings("unchecked")
//                Object key = typeParser.parse(entryKey, keyClass);
//                Object value = read(valueClass, entryValue);
//                map.put(key, value);
//              }
//            }
//          }
//          Object[] args = new Object[1];
//          args[0] = map;
//          descriptor.getWriteMethod().invoke(obj, args);
//        }
//
//      } else if (propertyClass.isArray()) {
//        final Type returnType = descriptor.getReadMethod().getGenericReturnType();
//        Class returnClass = (Class) returnType;
//        Class componentType = returnClass.getComponentType();
//        List<Configuration> prefixSet = configuration.prefixList(propertyName);
//        Object result = Array.newInstance(componentType, prefixSet.size());
//        for (int i = 0; i < prefixSet.size(); i++) {
//          Configuration configuration1 = prefixSet.get(i);
//          String value = configuration1.asMap().get("");
//          if (value != null) {
//            @SuppressWarnings("unchecked")
//            Object element = typeParser.parse(value.trim(), componentType);
//            Array.set(result, i, element);
//          }
//        }
//        Object[] args = new Object[1];
//        args[0] = result;
//        descriptor.getWriteMethod().invoke(obj, args);
//      } else if (propertyClass.isEnum()) {
//        @SuppressWarnings("unchecked")
//        Class<Enum<?>> enumType = (Class<Enum<?>>) propertyClass;
//        Optional<String> optional = configuration.getString(propertyName);
//        if (optional.isPresent()) {
//          Object o = Enum.valueOf(enumType.asSubclass(Enum.class), optional.get().trim());
//          Object[] args = new Object[1];
//          args[0] = o;
//          descriptor.getWriteMethod().invoke(obj, args);
//        }
//      } else {
//        Optional<String> optional = configuration.getString(propertyName);
//        if (optional.isPresent()) {
//          String input = optional.get().trim();
//          //查找静态类 例如 User user = User.create(String username);
//          MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
//          boolean methodFound = false;
//          Method method = null;
//          Object argument = null;
//          for (MethodDescriptor methodDescriptor : methodDescriptors) {
//            Method m = methodDescriptor.getMethod();
//            if (isStatic(m.getModifiers()) &&
//                m.getParameterTypes().length == 1 &&
//                m.getDeclaringClass().isAssignableFrom(m.getReturnType())) {
//              Type argType = m.getGenericParameterTypes()[0];
//              argument = typeParser.parseType(input, argType);
//              method = m;
//              methodFound = true;
//              break;
//            }
//          }
//          if (methodFound) {
//            method.setAccessible(true);
//            Object o = method.invoke(null, argument);
//            Object[] args = new Object[1];
//            args[0] = o;
//            descriptor.getWriteMethod().invoke(obj, args);
//          } else {
//            //查找只有一个参数的Constructor
//            boolean constructorFound = false;
//            Constructor<?> constructor = null;
//            for (Constructor<?> c : propertyClass.getDeclaredConstructors()) {
//              if (c.getGenericParameterTypes().length == 1) {
//                Type argType = c.getGenericParameterTypes()[0];
//                argument = typeParser.parseType(input, argType);
//                constructor = c;
//                constructorFound = true;
//                break;
//              }
//            }
//            if (constructorFound) {
//              constructor.setAccessible(true);
//              Object o = constructor.newInstance(argument);
//              Object[] args = new Object[1];
//              args[0] = o;
//              descriptor.getWriteMethod().invoke(obj, args);
//            }
//          }
//        }
//      }
//    }
//    return obj;
//  }
//
//  public boolean isBasicType(Class<?> clazz) {
//    if (clazz.isPrimitive()) {
//      return true;
//    }
//    return basicTypes.contains(clazz);
//  }
//
//  private Set<Type> getBasicTypes() {
//    Set<Type> ret = new HashSet<>();
//    for (Parsers parsers : Parsers.values()) {
//      ret.addAll(Arrays.asList(parsers.getTypes()));
//    }
//    return ret;
//  }
//
//
//  private <T> Collection<T> instantiateCollection(Class<? extends T> collectionType) {
//    if (collectionType.isInterface()) {
//      return instantiateCollectionFromInterface(collectionType);
//    }
//    return instantiateCollectionFromClass(collectionType);
//  }
//
//  @SuppressWarnings("unchecked")
//  private <T> Collection<T> instantiateCollectionFromClass(
//      Class<? extends T> collectionType) {
//    try {
//      return (Collection<T>) collectionType.getDeclaredConstructor().newInstance();
//    } catch (Exception e) {
//      String message = "Cannot instantiate collection of type '%s'";
//      throw new UnsupportedOperationException(String.format(message, collectionType), e);
//    }
//  }
//
//  @SuppressWarnings({"SortedCollectionWithNonComparableKeys"})
//  private <T> Collection<T> instantiateCollectionFromInterface(
//      Class<? extends T> collectionType) {
//    if (List.class.isAssignableFrom(collectionType)) {
//      return new ArrayList<>();
//    } else if (SortedSet.class.isAssignableFrom(collectionType)) {
//      return new TreeSet<>();
//    } else if (Set.class.isAssignableFrom(collectionType)) {
//      return new LinkedHashSet<>();
//    } else if (BlockingDeque.class.isAssignableFrom(collectionType)) {
//      return new LinkedBlockingDeque<>();
//    } else if (Deque.class.isAssignableFrom(collectionType)) {
//      return new ArrayDeque<>();
//    } else if (BlockingQueue.class.isAssignableFrom(collectionType)) {
//      return new LinkedBlockingDeque<>();
//    } else if (Queue.class.isAssignableFrom(collectionType)) {
//      return new LinkedList<>();
//    }
//    return new ArrayList<>();
//  }
//
//  private <K, V> Map<K, V> instantiateMap(Class<?> rawType) {
//    if (rawType.isInterface()) {
//      return instantiateMapFromInterface(rawType);
//    }
//    return instantiateMapFromClass(rawType);
//  }
//
//  @SuppressWarnings("SortedCollectionWithNonComparableKeys")
//  private <K, V> Map<K, V> instantiateMapFromInterface(Class<?> targetType) {
//    if (NavigableMap.class.isAssignableFrom(targetType)) {
//      return new ConcurrentSkipListMap<>();
//    }
//    if (ConcurrentMap.class.isAssignableFrom(targetType)) {
//      return new ConcurrentHashMap<>();
//    }
//    if (SortedMap.class.isAssignableFrom(targetType)) {
//      return new TreeMap<>();
//    }
//    return new LinkedHashMap<>();
//  }
//
//  @SuppressWarnings("unchecked")
//  private <K, V> Map<K, V> instantiateMapFromClass(Class<?> rawType) {
//    try {
//      return (Map<K, V>) rawType.getDeclaredConstructor().newInstance();
//    } catch (Exception e) {
//      String message = String.format("Cannot create map instance of type '%s'", rawType);
//      throw new UnsupportedOperationException(message, e);
//    }
//  }
//}
