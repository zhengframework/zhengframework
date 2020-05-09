package com.github.zhengframework.remoteconfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@SuppressWarnings("SpellCheckingInspection")
@ConfigurationInfo(prefix = "zheng.remoteconfig")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteConfigServerConfig implements ConfigurationDefine {

  private boolean enable=true;
}
