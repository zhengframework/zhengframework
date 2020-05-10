package com.github.zhengframework.bootstrap;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import com.google.inject.Stage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@ConfigurationInfo(prefix = "zheng.guice")
public class GuiceConfig implements ConfigurationDefine {

  private Stage stage = Stage.DEVELOPMENT;

}
