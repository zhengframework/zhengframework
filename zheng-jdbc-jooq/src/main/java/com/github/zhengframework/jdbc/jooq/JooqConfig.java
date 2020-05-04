package com.github.zhengframework.jdbc.jooq;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@NoArgsConstructor
@Data
@ConfigurationInfo(prefix = "zheng.jooq", supportGroup = true)
public class JooqConfig implements ConfigurationDefine {

  private boolean enable = true;

}
