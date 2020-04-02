package com.dadazhishi.zheng.rest.jersey;

import com.dadazhishi.zheng.service.Scanner;
import com.google.inject.Injector;
import javax.ws.rs.ext.Provider;

public class ProviderScanner extends Scanner<Provider> {

  public ProviderScanner(Injector injector) {
    super(injector, Provider.class);
  }
}
