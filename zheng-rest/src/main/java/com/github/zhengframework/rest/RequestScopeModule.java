package com.github.zhengframework.rest;

/*-
 * #%L
 * zheng-rest
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
