package com.dadazhishi.zheng.metrics.micrometer;

import com.google.inject.AbstractModule;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;

public class MicrometerModule extends AbstractModule {

  private final CompositeMeterRegistry meterRegistry;

  public MicrometerModule() {
    this(new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM));
  }

  public MicrometerModule(
      MeterRegistry... meterRegistries) {
    this.meterRegistry = new CompositeMeterRegistry();
    for (MeterRegistry registry : meterRegistries) {
      this.meterRegistry.add(registry);
    }
    new ClassLoaderMetrics().bindTo(meterRegistry);
    new JvmMemoryMetrics().bindTo(meterRegistry);
    new JvmGcMetrics().bindTo(meterRegistry);
    new ProcessorMetrics().bindTo(meterRegistry);
    new JvmThreadMetrics().bindTo(meterRegistry);
  }

  @Override
  protected void configure() {
    bind(MeterRegistry.class).toInstance(meterRegistry);
    bind(TimedAspect.class).toInstance(new TimedAspect(meterRegistry));
    bind(CountedAspect.class).toInstance(new CountedAspect(meterRegistry));

  }

}
