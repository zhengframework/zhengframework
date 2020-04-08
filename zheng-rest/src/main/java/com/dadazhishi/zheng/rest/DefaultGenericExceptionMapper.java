package com.dadazhishi.zheng.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Singleton
@Provider
public class DefaultGenericExceptionMapper implements ExceptionMapper<Throwable> {

  private static final int DEFAULT_STATUS_CODE = Response.Status.INTERNAL_SERVER_ERROR
      .getStatusCode();

  private static String toStatusText(int status) {
    val statusType = Response.Status.fromStatusCode(status);
    return statusType != null
        ? statusType.getReasonPhrase()
        : Integer.toString(status);
  }

  @Override
  public Response toResponse(Throwable e) {
    final int status = (e instanceof WebApplicationException)
        ? ((WebApplicationException) e).getResponse().getStatus()
        : DEFAULT_STATUS_CODE;

    log.error("error: status: " + status, e);

    val message = e.getMessage();
    val saneMessage = message == null || message.startsWith("RESTEASY")
        ? toStatusText(status)
        : e.getMessage();
    return Response.status(status)
        .entity(new GenericError(status, saneMessage))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }

  @Getter
  public static class GenericError {

    private final int code;

    private final String message;


    @JsonCreator
    @Builder(toBuilder = true)
    public GenericError(@JsonProperty("code") int code, @JsonProperty("code") String message) {
      this.code = code;
      this.message = message;
    }
  }

}