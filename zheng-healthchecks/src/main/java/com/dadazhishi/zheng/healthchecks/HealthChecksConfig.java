package com.github.zhengframework.healthchecks;

import io.dropwizard.util.Duration;
import lombok.Data;

@Data
public class HealthChecksConfig {

  /**
   * health checks which extend AbstractMetricsHealthCheck will register metrics with this prefix.
   */
  private String metricsPrefix = "zheng.healthChecks";
  /**
   * <p>Run health checks periodically at this interval. Failing health checks will be logged at
   * level WARN. This accepts a flexible format like "10 minutes" or "30s".</p>
   *
   * Default is null, which disables periodic health checks.
   */
  private Duration interval = Duration.minutes(10);

  public void setDuration(Duration value) {
    this.interval = value;
  }
}
