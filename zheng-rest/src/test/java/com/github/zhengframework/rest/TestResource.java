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

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path(TestResource.PATH)
public class TestResource {

  public static final String PATH = "test";
  public static final String MESSAGE = "Hello World!";

  private final MySingleton service;
  private final PerRequestService requestService;

  @Inject
  TestResource(MySingleton service, PerRequestService perRequestService) {
    this.service = service;
    this.requestService = perRequestService;
  }

  @Path("inject")
  @GET
  public String get() {
    return "Success: " + service.call() + ", " + requestService.call();
  }

  @GET
  public String sayHello() {
    return MESSAGE;
  }

}
