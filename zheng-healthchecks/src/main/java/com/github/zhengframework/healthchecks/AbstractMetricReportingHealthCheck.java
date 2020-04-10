package com.github.zhengframework.healthchecks;

import com.codahale.metrics.CachedGauge;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import io.dropwizard.util.Duration;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * classes which derive from this class will expose their healthcheck as a Gauge Metric. So, the
 * health check will be run whenever metrics are reported.
 *
 * By default, gwizard-metrics creates a JmxReporter. So, if any tool is scraping the JMX metrics
 * tree, the health checks will be run at whatever rate the tools is performing JMX queries that
 * include the Metrics MBeans.
 *
 * The name of the metric will be constructed fromt he prefix read from the configuration, plus the
 * name passed to the ctor. By default, the metric mbean will have the ObjectName: {@code
 * metrics:name=gwizard.healthChecks.&lt;healthcheckName&gt;}
 */
@Slf4j
public abstract class AbstractMetricReportingHealthCheck extends HealthCheck {

  private final String healthCheckName;
  private Duration cacheInterval = null;


  public AbstractMetricReportingHealthCheck(HealthChecks healthChecks, String healthCheckName) {
    this.healthCheckName = healthCheckName;
    healthChecks.add(healthCheckName, this);
  }


  public AbstractMetricReportingHealthCheck(HealthChecks healthChecks, String healthCheckName,
      Duration cacheInterval) {
    this(healthChecks, healthCheckName);
    this.cacheInterval = cacheInterval;
  }

  /**
   * calls the health check's check() method, and converts the returned value to an integer.
   *
   * @return Integer value representing the result of the health check (1: healthy, 0: unhealthy,
   * null: exception during check)
   */
  private Integer checkAndConvert() {
    try {
      Result result = check();
      if (!result.isHealthy()) {
        log.warn("{} : unhealthy - {}", healthCheckName, Strings.nullToEmpty(result.getMessage()),
            result.getError());
      }
      return result.isHealthy() ? 1 : 0;
    } catch (Exception e) {
      log.warn("exception performing health check: ", e.getMessage());
      return null;
    }
  }

  @Inject
  private void init(HealthChecksConfig healthChecksConfig, MetricRegistry metricRegistry) {
    Metric m;
    Optional<Duration> cacheInterval = Optional.ofNullable(this.cacheInterval);
    if (cacheInterval.isPresent()) {
      m = new CachedGauge<Integer>(cacheInterval.get().getQuantity(),
          cacheInterval.get().getUnit()) {
        @Override
        protected Integer loadValue() {
          return checkAndConvert();
        }
      };

    } else {
      m = (Gauge<Integer>) this::checkAndConvert;
    }
    metricRegistry
        .register(MetricRegistry.name(healthChecksConfig.getMetricsPrefix(), healthCheckName), m);
  }
}
