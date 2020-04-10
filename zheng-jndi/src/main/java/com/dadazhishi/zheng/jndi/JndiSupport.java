package com.github.zhengframework.jndi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.naming.Context;
import javax.naming.NamingException;

public class JndiSupport {

  private JndiSupport() {
  }

  public static <T> Provider<T> fromJndi(Class<T> type, String name) {
    return new JndiProvider<T>(type, name);
  }

  static class JndiProvider<T> implements Provider<T> {

    final Class<T> type;
    final String name;
    @Inject
    Context context;

    public JndiProvider(Class<T> type, String name) {
      this.type = type;
      this.name = name;
    }

    @Override
    public T get() {
      try {
        return type.cast(context.lookup(name));
      } catch (NamingException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
