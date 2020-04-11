package com.github.zhengframework.web;

import io.undertow.Undertow;
import java.io.IOException;
import javax.inject.Provider;
import org.xnio.OptionMap;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

public class UndertowServerProvider implements Provider<Undertow.Builder> {

  @Override
  public Undertow.Builder get() {
    try {
      Xnio nio = Xnio.getInstance("nio", Undertow.class.getClassLoader());
      XnioWorker worker = nio.createWorker(OptionMap.builder().getMap());
      return Undertow.builder().setWorker(worker);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
