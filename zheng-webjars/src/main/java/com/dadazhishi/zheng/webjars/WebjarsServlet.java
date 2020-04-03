package com.dadazhishi.zheng.webjars;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringJoiner;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.webjars.NotFoundException;
import org.webjars.WebJarAssetLocator;

@Slf4j
public class WebjarsServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final long DEFAULT_EXPIRE_TIME_MS = 86400000L; // 1 day
  private static final long DEFAULT_EXPIRE_TIME_S = 86400L; // 1 day
  /**
   * The default buffer size ({@value}) to use
   */
  private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
  private static final int EOF = -1;
  private final WebjarsConfig webjarsConfig;
  private boolean disableCache = false;
  private WebJarAssetLocator locator;

  @Inject
  public WebjarsServlet(WebjarsConfig webjarsConfig) {
    this.webjarsConfig = webjarsConfig;
  }

  private static boolean isDirectoryRequest(String uri) {
    return uri.endsWith("/");
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
    int n = 0;
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
  public void init() throws ServletException {

    locator = new WebJarAssetLocator();
    ServletConfig config = getServletConfig();
    if (config == null) {
      throw new NullPointerException(
          "Expected servlet container to provide a non-null ServletConfig.");
    }
    try {
      String disableCache = config.getInitParameter("disableCache");
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
    String[] strings = uri.split("/");
    StringJoiner stringJoiner = new StringJoiner("/");
    for (int i = 2; i < strings.length; i++) {
      stringJoiner.add(strings[i]);
    }
    return locator.getFullPath(strings[1], stringJoiner.toString());
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    log.debug("requestURI: {}", request.getRequestURI());
    String uri = request.getRequestURI()
        .replaceFirst(request.getContextPath(), "")
        .replaceFirst(webjarsConfig.getPath(), "");
    if (isDirectoryRequest(uri)) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    String webjarsResourceURI = getFullPath(uri);

//    String webjarsResourceURI = "/META-INF/resources" + uri;
    log.debug("Webjars resource requested: {}", webjarsResourceURI);

    String eTagName;
    try {
      eTagName = this.getETagName(webjarsResourceURI);
    } catch (IllegalArgumentException e) {
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

  /**
   *
   */
  private String getFileName(String webjarsResourceURI) {
    String[] tokens = webjarsResourceURI.split("/");
    return tokens[tokens.length - 1];
  }

  /**
   * @throws IllegalArgumentException when insufficient URI has given
   */
  private String getETagName(String webjarsResourceURI) {

    String[] tokens = webjarsResourceURI.split("/");
    if (tokens.length < 7) {
      throw new IllegalArgumentException("insufficient URL has given: " + webjarsResourceURI);
    }
    String version = tokens[5];
    String fileName = tokens[tokens.length - 1];

    return fileName + "_" + version;
  }

  /* Important!!*/
  /* The code bellow has been copied from apache Commons IO. More specifically from its IOUtils class. */
  /* The reason is becasue I don't want to include any more dependencies */

  /**
   *
   */
  private boolean checkETagMatch(HttpServletRequest request, String eTagName) {

    String token = request.getHeader("If-None-Match");
    return (token != null && token.equals(eTagName));
  }

  /**
   *
   */
  private boolean checkLastModify(HttpServletRequest request) {

    long last = request.getDateHeader("If-Modified-Since");
    return (last != -1L && (last - System.currentTimeMillis() > 0L));
  }

  // copy from InputStream
  //-----------------------------------------------------------------------

  /**
   *
   */
  private void prepareCacheHeaders(HttpServletResponse response, String eTag) {

    response.setHeader("ETag", eTag);
    response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME_MS);
    response.addDateHeader("Last-Modified", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME_MS);
    response.addHeader("Cache-Control", "private, max-age=" + DEFAULT_EXPIRE_TIME_S);
  }
}