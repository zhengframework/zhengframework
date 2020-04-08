package com.dadazhishi.zheng.configuration.ex;

public class MissingEnvironmentException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private static final String MISSING_ENV_MSG = "Missing environment: ";

  public MissingEnvironmentException(String envName) {
    super(MISSING_ENV_MSG + envName);
  }

  public MissingEnvironmentException(String envName, Throwable cause) {
    super(MISSING_ENV_MSG + envName, cause);
  }
}
