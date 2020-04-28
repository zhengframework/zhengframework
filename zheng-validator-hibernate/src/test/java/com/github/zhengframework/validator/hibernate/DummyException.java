package com.github.zhengframework.validator.hibernate;

public class DummyException extends Exception {

  private static final long serialVersionUID = 1L;

  public DummyException(Throwable cause) {
    super(cause);
  }

  public DummyException(String message, Throwable cause) {
    super(message, cause);
  }

}