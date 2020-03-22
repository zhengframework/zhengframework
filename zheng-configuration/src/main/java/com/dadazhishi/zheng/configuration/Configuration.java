package com.dadazhishi.zheng.configuration;

import java.util.Map;

public interface Configuration extends Map<String, String> {

  Configuration prefix(String prefix);

  Configuration prefixWithoutHead(String prefix);

}
