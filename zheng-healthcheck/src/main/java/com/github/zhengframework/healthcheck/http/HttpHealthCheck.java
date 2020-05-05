package com.github.zhengframework.healthcheck.http;

/*-
 * #%L
 * zheng-healthcheck
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

import com.github.zhengframework.healthcheck.NamedHealthCheck;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

@Slf4j
public class HttpHealthCheck extends NamedHealthCheck {

  static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(2);

  private final String url;
  private final Duration readTimeout;
  private final Duration connectionTimeout;

  public HttpHealthCheck(final String url,
      final Duration readTimeout,
      final Duration connectionTimeout) {
    this.url = Objects.requireNonNull(url);
    this.readTimeout = readTimeout;
    this.connectionTimeout = connectionTimeout;
    Preconditions.checkState(readTimeout.toMillis() > 0L);
    Preconditions.checkState(connectionTimeout.toMillis() > 0L);
  }

  public HttpHealthCheck(String url) {
    this(url, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
  }

  @Override
  public String getName() {
    return "HttpHealthCheck-" + url;
  }

  @Override
  protected Result check() throws Exception {
    final HttpHealthResponse httpHealthResponse = httpCheck(url);

    if (isHealthResponseValid(httpHealthResponse)) {
      log.debug("Health check against url={} successful", url);
      return Result.healthy();
    }

    log.debug("Health check against url={} failed with response={}", url, httpHealthResponse);
    return Result.unhealthy("Http health check against url=%s failed with response=%s", url,
        httpHealthResponse);
  }

  protected HttpHealthResponse httpCheck(final String input) throws IOException {
    URL url = new URL(input);
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    urlConnection.setConnectTimeout((int) connectionTimeout.toMillis());
    urlConnection.setReadTimeout((int) readTimeout.toMillis());
    urlConnection.setRequestMethod("GET");
    urlConnection.connect();
    return new HttpHealthResponse(urlConnection.getResponseCode(),
        urlConnection.getResponseMessage(),
        IOUtils.toString(urlConnection.getInputStream(), Charset.defaultCharset()));
  }

  protected boolean isHealthResponseValid(final HttpHealthResponse httpHealthResponse) {
    return httpHealthResponse.getStatus() >= 200 && httpHealthResponse.getStatus() < 300;
  }
}
