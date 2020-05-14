package com.github.zhengframework.webjars;

/*-
 * #%L
 * zheng-webjars
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.StringJoiner;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.webjars.NotFoundException;
import org.webjars.WebJarAssetLocator;

@Slf4j
public class WebjarsServlet extends HttpServlet {

  public static final String DISABLE_CACHE = "disableCache";

  private static final long DEFAULT_EXPIRE_TIME_MS = 86400000L; // 1 day
  private static final long DEFAULT_EXPIRE_TIME_S = 86400L; // 1 day

  private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
  private static final int EOF = -1;
  private static final long serialVersionUID = -2107109556895489409L;
  private final transient WebjarsConfig webjarsConfig;
  private boolean disableCache = false;
  private transient WebJarAssetLocator locator = new WebJarAssetLocator();

  @Inject
  public WebjarsServlet(WebjarsConfig webjarsConfig) {
    this.webjarsConfig = webjarsConfig;
  }

  private static boolean isDirectoryRequest(String uri) {
    return uri.endsWith("/");
  }

  /**
   * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
   *
   * <p>This method buffers the input internally, so there is no need to use a <code>
   * BufferedInputStream</code>.
   *
   * <p>Large streams (over 2GB) will return a bytes copied value of <code>-1</code> after the copy
   * has completed since the correct number of bytes cannot be returned as an int. For large streams
   * use the <code>copyLarge(InputStream, OutputStream)</code> method.
   *
   * @param input the <code>InputStream</code> to read from
   * @param output the <code>OutputStream</code> to write to
   * @throws NullPointerException if the input or output is null
   * @throws IOException if an I/O error occurs
   * @since 1.1
   */
  private static void copy(InputStream input, OutputStream output) throws IOException {
    int n;
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    while (EOF != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
    }
  }

  @Override
  public void init() {

    ServletConfig config = getServletConfig();
    if (config == null) {
      throw new NullPointerException(
          "Expected servlet container to provide a non-null ServletConfig.");
    }
    try {
      String disableCache = config.getInitParameter(DISABLE_CACHE);
      if (disableCache != null) {
        this.disableCache = Boolean.parseBoolean(disableCache);
        log.info("WebjarsServlet cache enabled: {}", !this.disableCache);
      }
    } catch (Exception e) {
      log.warn("The WebjarsServlet configuration parameter \"disableCache\" is invalid");
    }
    log.info("WebjarsServlet initialization completed");
  }

  private String getFullPath(String uri) throws NotFoundException {
    log.info("uri={}", uri);
    if (uri.startsWith("/")) {
      uri = uri.substring(1);
    }
    String[] strings = uri.split("/");
    log.info("str={}", Arrays.toString(strings));
    StringJoiner stringJoiner = new StringJoiner("/");
    for (int i = 1; i < strings.length; i++) {
      stringJoiner.add(strings[i]);
    }
    try {
      return locator.getFullPath(strings[0], stringJoiner.toString());
    } catch (NullPointerException e) {
      log.error("get full path error, uri={}", uri, e);
      throw new NotFoundException(uri);
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    log.info("requestURI: {}", request.getRequestURI());
    log.info("getContextPath: {}", request.getContextPath());
    String uri =
        request
            .getRequestURI()
            .replaceFirst(request.getContextPath(), "")
            .replaceFirst(webjarsConfig.getBasePath(), "");
    if (isDirectoryRequest(uri)) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    String webjarsResourceURI;
    try {
      webjarsResourceURI = getFullPath(uri);
    } catch (NotFoundException e) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    log.info("Webjars resource requested: {}", webjarsResourceURI);

    String eTagName;
    try {
      eTagName = this.getETagName(webjarsResourceURI);
    } catch (IllegalArgumentException e) {
      log.info("etag");
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    if (!disableCache) {
      if (checkETagMatch(request, eTagName) || checkLastModify(request)) {
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        return;
      }
    }

    InputStream inputStream = this.getClass().getResourceAsStream("/" + webjarsResourceURI);
    if (inputStream != null) {
      try {
        if (!disableCache) {
          prepareCacheHeaders(response, eTagName);
        }
        String filename = getFileName(webjarsResourceURI);
        String mimeType = this.getServletContext().getMimeType(filename);

        response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
        copy(inputStream, response.getOutputStream());
      } finally {
        inputStream.close();
      }
    } else {
      log.error("read");
      // return HTTP error
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private String getFileName(String webjarsResourceURI) {
    String[] tokens = webjarsResourceURI.split("/");
    return tokens[tokens.length - 1];
  }

  private String getETagName(String webjarsResourceURI) {
    return webjarsResourceURI;
  }

  /* Important!!*/
  /* The code bellow has been copied from apache Commons IO. More specifically from its IOUtils class. */
  /* The reason is becasue I don't want to include any more dependencies */

  private boolean checkETagMatch(HttpServletRequest request, String eTagName) {

    String token = request.getHeader("If-None-Match");
    return (token != null && token.equals(eTagName));
  }

  private boolean checkLastModify(HttpServletRequest request) {

    long last = request.getDateHeader("If-Modified-Since");
    return (last != -1L && (last - System.currentTimeMillis() > 0L));
  }

  private void prepareCacheHeaders(HttpServletResponse response, String eTag) {

    response.setHeader("ETag", eTag);
    response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME_MS);
    response.addDateHeader("Last-Modified", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME_MS);
    response.addHeader("Cache-Control", "private, max-age=" + DEFAULT_EXPIRE_TIME_S);
  }
}
