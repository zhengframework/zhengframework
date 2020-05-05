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

import com.codahale.metrics.Timer;
import java.util.concurrent.TimeUnit;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * A method interceptor which times the execution of the annotated method.
 */
public class TimedInterceptor implements MethodInterceptor {

  private final Timer timer;

  public TimedInterceptor(Timer timer) {
    this.timer = timer;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    // Since these timers are always created via the default ctor (via MetricRegister#timer), they
    // always use
    // nanoTime, so we can save an allocation here by not using Context.
    long start = System.nanoTime();
    try {
      return invocation.proceed();
    } finally {
      timer.update(System.nanoTime() - start, TimeUnit.NANOSECONDS);
    }
  }
}
