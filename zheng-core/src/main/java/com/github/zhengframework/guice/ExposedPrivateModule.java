package com.github.zhengframework.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ExposedPrivateModule extends AbstractModule {

  private List<Key> exposeList = new ArrayList<>();

  public List<Key> getExposeList() {
    return exposeList;
  }

  protected void expose(Key key) {
    exposeList.add(key);
  }

  protected void expose(Class clazz) {
    exposeList.add(Key.get(clazz));
  }

}
