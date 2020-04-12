package com.github.zhengframework.guice;

public interface TypePostProcessor<T> {

  void process(T instance) throws Exception;
}
