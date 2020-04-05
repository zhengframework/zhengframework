package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.io.ClasspathLocationStrategy;
import com.dadazhishi.zheng.configuration.io.DefaultFileSystem;
import com.dadazhishi.zheng.configuration.io.FileLocator;
import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.InputStream;

public class ClasspathConfigurationResolver extends FileConfigurationResolver {

  public ClasspathConfigurationResolver(FileLocator fileLocator,
      ConfigurationParser<InputStream> parser,
      boolean failOnError) {
    super(new DefaultFileSystem(), new ClasspathLocationStrategy(), fileLocator, parser,
        failOnError);
  }

  public ClasspathConfigurationResolver(FileLocator fileLocator,
      ConfigurationParser<InputStream> parser) {
    this(fileLocator, parser, true);
  }
}
