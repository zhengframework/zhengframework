package com.github.zhengframework.configuration.parser;

public interface Parser<T> {

  T parse(String input);

}
