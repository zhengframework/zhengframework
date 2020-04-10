package com.github.zhengframework.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;

public class SettableGauge<T> implements Metric, Gauge<T> {

  private volatile T value = null;

  @Override
  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }
}
