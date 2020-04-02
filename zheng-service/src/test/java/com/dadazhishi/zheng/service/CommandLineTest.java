package com.dadazhishi.zheng.service;

import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class CommandLineTest {

  @Test
  public void test() throws ParseException {
    Options options = new Options();

    Option option = Option.builder("f").longOpt("file").hasArg(true)
        .desc("app configuration file path").build();
    options.addOption(option);
    CommandLineParser parser = new DefaultParser();
    String argsConfigFile = null;
    String[] args = new String[]{"-f", "app.properties" };
    CommandLine commandLine = parser.parse(options, args);
    if (commandLine.hasOption("file")) {
      argsConfigFile = commandLine.getOptionValue("file");
    }

    assertEquals("app.properties", argsConfigFile);

  }

}
