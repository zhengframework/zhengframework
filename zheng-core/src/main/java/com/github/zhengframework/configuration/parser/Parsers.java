//package com.github.zhengframework.configuration.parser;
//
//import java.lang.reflect.Type;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.text.NumberFormat;
//import java.text.ParseException;
//import java.util.LinkedHashMap;
//import java.util.Locale;
//import java.util.Map;
//
//public enum Parsers implements Parser<Object> {
//  BYTE(Byte.class, Byte.TYPE) {
//    public Byte parse(String input) {
//      return Byte.valueOf(input.trim());
//    }
//  },
//  INTEGER(Integer.class, Integer.TYPE) {
//    public Integer parse(String input) {
//      return Integer.valueOf(input.trim());
//    }
//  },
//  LONG(Long.class, Long.TYPE) {
//    public Long parse(String input) {
//      return Long.valueOf(input.trim());
//    }
//  },
//  SHORT(Short.class, Short.TYPE) {
//    public Short parse(String input) {
//      return Short.valueOf(input.trim());
//    }
//  },
//  FLOAT(Float.class, Float.TYPE) {
//    public Float parse(String input) {
//      return Float.valueOf(input);
//    }
//  },
//  DOUBLE(Double.class, Double.TYPE) {
//    public Double parse(String input) {
//      return Double.valueOf(input);
//    }
//  },
//  BOOLEAN(Boolean.class, Boolean.TYPE) {
//    public Boolean parse(String input) {
//      String value = input.trim().toLowerCase();
//      if ("true".equals(value)) {
//        return Boolean.TRUE;
//      } else if ("false".equals(value)) {
//        return Boolean.FALSE;
//      } else {
//        String message = "\"%s\" is not parsable to a Boolean.";
//        throw new IllegalArgumentException(String.format(message, input));
//      }
//    }
//  },
//  CHARACTER(Character.class, Character.TYPE) {
//    public Character parse(String input) {
//      if (input.length() == 1) {
//        return input.charAt(0);
//      } else {
//        String message = "\"%s\" must only contain a single character.";
//        throw new IllegalArgumentException(String.format(message, input));
//      }
//    }
//  },
//  BIG_INTEGER(BigInteger.class) {
//    public BigInteger parse(String input) {
//      return new BigInteger(input.trim());
//    }
//  },
//  BIG_DECIMAL(BigDecimal.class) {
//    public BigDecimal parse(String input) {
//      try {
//        return new BigDecimal(input.trim());
//      } catch (NumberFormatException var6) {
//        String message = "NumberFormatException For input string: \"" + input + "\"";
//        NumberFormatException e2 = new NumberFormatException(message);
//        e2.setStackTrace(var6.getStackTrace());
//        throw e2;
//      }
//    }
//  },
//  STRING(String.class) {
//    public String parse(String input) {
//      return input;
//    }
//  },
//  NUMBER(Number.class) {
//    private final ThreadLocal<NumberFormat> NUMBER_FORMAT = new ThreadLocal<NumberFormat>() {
//      protected NumberFormat initialValue() {
//        return NumberFormat.getInstance(Locale.US);
//      }
//    };
//
//    public Number parse(String input) {
//      try {
//        return this.NUMBER_FORMAT.get().parse(input.trim());
//      } catch (ParseException var4) {
//        throw new IllegalArgumentException(var4.getMessage(), var4);
//      }
//    }
//  };
//
//
//  private static final Map<Type, Parser<?>> DEFAULT_PARSERS = new LinkedHashMap<>();
//
//  static {
//    for (Parsers parsers : values()) {
//      for (Type type : parsers.types) {
//        DEFAULT_PARSERS.put(type, decorateParser(type, parsers));
//      }
//    }
//  }
//
//  public Type[] getTypes() {
//    return types;
//  }
//
//  private Type[] types;
//
//  Parsers(Type... types) {
//    this.types = types;
//  }
//
//  public static Map<Type, Parser<?>> copyDefault() {
//    return new LinkedHashMap<>(DEFAULT_PARSERS);
//  }
//
//  @SuppressWarnings("rawtypes")
//  static <T> Parser<T> decorateParser(Type targetType, final Parser<T> parser) {
//    if (targetType instanceof Class) {
//      Class<?> c = (Class) targetType;
//      if (c.isPrimitive()) {
//        return input -> {
//          if (input == null) {
//            throw new UnsupportedOperationException("Primitive can't be set to null");
//          } else {
//            return parser.parse(input);
//          }
//        };
//      }
//    }
//
//    return input -> input == null ? null : parser.parse(input);
//
//  }
//}
