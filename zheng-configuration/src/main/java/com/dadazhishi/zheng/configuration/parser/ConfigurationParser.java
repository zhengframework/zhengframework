package com.dadazhishi.zheng.configuration.parser;


import java.util.Map;

public interface ConfigurationParser<T> {

  Map<String, String> parse(T content);

}
