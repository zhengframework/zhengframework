package com.palominolabs.metrics.guice;

/*-
 * #%L
 * zheng-metrics
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

import com.codahale.metrics.Meter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * A method interceptor which measures the rate at which the annotated method throws exceptions of a
 * given type.
 */
public class ExceptionMeteredInterceptor implements MethodInterceptor {

  private final Meter meter;
  private final Class<? extends Throwable> klass;

  public ExceptionMeteredInterceptor(Meter meter, Class<? extends Throwable> klass) {
    this.meter = meter;
    this.klass = klass;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    try {
      return invocation.proceed();
    } catch (Throwable t) {
      if (klass.isAssignableFrom(t.getClass())) {
        meter.mark();
      }
      throw t;
    }
  }
}
