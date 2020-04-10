package com.github.zhengframework.service;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.Getter;

public class Arguments {

  @Getter
  private final OptionParser optionParser = new OptionParser();

  private final String[] arguments;

  public Arguments(String[] arguments) {
    this.arguments = arguments;
    optionParser.allowsUnrecognizedOptions();
  }

  public OptionSet parse() {
    return optionParser.parse(arguments);
  }

}
