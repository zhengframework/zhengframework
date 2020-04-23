package com.github.zhengframework.rest;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.jboss.resteasy.core.ResteasyContext;

public class RequestScopeModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Request.class).toProvider(new ResteasyContextProvider<>(Request.class))
        .in(RequestScoped.class);
    bind(HttpHeaders.class).toProvider(new ResteasyContextProvider<>(HttpHeaders.class))
        .in(RequestScoped.class);
    bind(UriInfo.class).toProvider(new ResteasyContextProvider<>(UriInfo.class))
        .in(RequestScoped.class);
    bind(SecurityContext.class)
        .toProvider(new ResteasyContextProvider<>(SecurityContext.class))
        .in(RequestScoped.class);
  }

  private static class ResteasyContextProvider<T> implements Provider<T> {

    private final Class<T> instanceClass;

    ResteasyContextProvider(final Class<T> instanceClass) {
      this.instanceClass = instanceClass;
    }

    @Override
    public T get() {
      return ResteasyContext.getContextData(instanceClass);
    }
  }
}
