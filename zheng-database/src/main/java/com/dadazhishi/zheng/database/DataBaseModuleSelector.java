package com.dadazhishi.zheng.database;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.ServiceLoader;

public class DataBaseModuleSelector {

  public static AbstractDataBaseModule select(DataBaseConfig config) {
    ServiceLoader<AbstractDataBaseModule> abstractDataBaseModules = ServiceLoader
        .load(AbstractDataBaseModule.class);
    Iterator<AbstractDataBaseModule> iterator = abstractDataBaseModules.iterator();
    if (iterator.hasNext()) {
      AbstractDataBaseModule next = iterator.next();
      try {
        Constructor<? extends AbstractDataBaseModule> constructor = next.getClass()
            .getConstructor(DataBaseConfig.class);
        return constructor.newInstance(config);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    throw new RuntimeException("can not find any one AbstractDataBaseModule");
  }

  public static AbstractDataBaseModule select() {
    ServiceLoader<AbstractDataBaseModule> abstractDataBaseModules = ServiceLoader
        .load(AbstractDataBaseModule.class);

    Iterator<AbstractDataBaseModule> iterator = abstractDataBaseModules.iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    }
    throw new RuntimeException("can not find any one AbstractDataBaseModule");
  }

}
