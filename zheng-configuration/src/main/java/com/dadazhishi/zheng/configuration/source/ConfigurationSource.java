package com.dadazhishi.zheng.configuration.source;

import java.io.InputStream;
import java.net.URI;
import java.util.function.Supplier;

public interface ConfigurationSource {

  Supplier<InputStream> getSource(URI uri);

}
