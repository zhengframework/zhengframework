package com.github.zhengframework.swagger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.webjars.NotFoundException;
import org.webjars.WebJarAssetLocator;

@Slf4j
public class SwaggerUIServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static final long DEFAULT_EXPIRE_TIME_MS = 86400000L; // 1 day
  private static final long DEFAULT_EXPIRE_TIME_S = 86400L; // 1 day
  /**
   * The default buffer size ({@value}) to use
   */
  private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
  private static final int EOF = -1;
  private final SwaggerConfig swaggerConfig;
  private final String indexContent;
  private boolean disableCache = false;
  private WebJarAssetLocator locator = new WebJarAssetLocator();

  @Inject
  public SwaggerUIServlet(SwaggerConfig swaggerConfig) {
    this.swaggerConfig = swaggerConfig;

    URL resource = SwaggerUIServlet.class.getResource("/swagger-ui/index.html");
    try (InputStream inputStream = resource.openStream()) {
      String string = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
      indexContent = string.replace("http://127.0.0.1:8080/openapi.json", swaggerConfig.getApiUrl());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * Copy bytes from an <code>InputStream</code> to an
   * <code>OutputStream</code>.
   * <p>
   * This method buffers the input internally, so there is no need to use a
   * <code>BufferedInputStream</code>.
   * <p>
   * Large streams (over 2GB) will return a bytes copied value of
   * <code>-1</code> after the copy has completed since the correct
   * number of bytes cannot be returned as an int. For large streams use the
   * <code>copyLarge(InputStream, OutputStream)</code> method.
   *
   * @param input the <code>InputStream</code> to read from
   * @param output the <code>OutputStream</code> to write to
   * @return the number of bytes copied, or -1 if &gt; Integer.MAX_VALUE
   * @throws NullPointerException if the input or output is null
   * @throws IOException if an I/O error occurs
   * @since 1.1
   */
  private static int copy(InputStream input, OutputStream output) throws IOException {
    long count = 0;
    int n;
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    while (EOF != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    }
    if (count > Integer.MAX_VALUE) {
      return -1;
    }
    return (int) count;
  }

  @Override
  public void init() {
    log.info("WebjarsServlet initialization completed");
  }

  private String getFullPath(String webjar, String uri) throws NotFoundException {
    log.info("uri={}", uri);
    if (uri.startsWith("/")) {
      uri = uri.substring(1);
    }
    try {
      return locator.getFullPath(webjar, uri);
    } catch (NullPointerException e) {
      log.error("get full path error, uri={}", uri, e);
      throw new NotFoundException(uri);
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String eTagName;
    String uri = request.getRequestURI()
        .replaceFirst(request.getContextPath(), "")
        .replaceFirst(swaggerConfig.getUiPath(), "");
    if (uri.endsWith("/")) {
      uri = uri + "index.html";
    }
    if (uri.endsWith("/index.html")) {
      eTagName = swaggerConfig.getUiPath() + "/index.html";

      if (!disableCache) {
        prepareCacheHeaders(response, eTagName);
      }
      String filename = getFileName(uri);
      String mimeType = this.getServletContext().getMimeType(filename);
      response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
      IOUtils.write(indexContent, response.getOutputStream(), StandardCharsets.UTF_8);
      return;
    }
    String webjarsResourceURI;
    try {
      webjarsResourceURI = getFullPath("swagger-ui", uri);
    } catch (NotFoundException e) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    log.debug("Webjars resource requested: {}", webjarsResourceURI);

    try {
      eTagName = this.getETagName(webjarsResourceURI);
    } catch (IllegalArgumentException e) {
      log.info("etag");
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    if (!disableCache) {
      if (checkETagMatch(request, eTagName)
          || checkLastModify(request)) {
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
      // return HTTP error
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private String getFileName(String webjarsResourceURI) {
    String[] tokens = webjarsResourceURI.split("/");
    return tokens[tokens.length - 1];
  }

  /**
   * @param webjarsResourceURI webjarsResourceURI
   * @return ETag name
   * @throws IllegalArgumentException when insufficient URI has given
   */
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

  // copy from InputStream
  //-----------------------------------------------------------------------

  private void prepareCacheHeaders(HttpServletResponse response, String eTag) {

    response.setHeader("ETag", eTag);
    response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME_MS);
    response.addDateHeader("Last-Modified", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME_MS);
    response.addHeader("Cache-Control", "private, max-age=" + DEFAULT_EXPIRE_TIME_S);
  }
}
