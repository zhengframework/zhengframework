package com.github.zhengframework.remoteconfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public final class RemoteConfigRequest {

  private String[] configKeys;
  private List<ConfigParam> configParams;


  public static RemoteConfigRequestBuilder builder() {
    return new RemoteConfigRequestBuilder();
  }

  public static final class RemoteConfigRequestBuilder {

    private String[] configKeys;
    private List<ConfigParam> configParams;

    private RemoteConfigRequestBuilder() {
    }

    public RemoteConfigRequestBuilder configKeys(String... configKeys) {
      this.configKeys = Objects.requireNonNull(configKeys, "configKeys is null");
      return this;
    }

    public RemoteConfigRequestBuilder configParams(List<ConfigParam> configParams) {
      if (this.configParams == null) {
        this.configParams = new ArrayList<>();
      }
      this.configParams.addAll(
          Objects.requireNonNull(configParams, "configParams is null"));
      return this;
    }

    public RemoteConfigRequestBuilder configParams(ConfigParam... configParams) {
      if (this.configParams == null) {
        this.configParams = new ArrayList<>();
      }
      this.configParams.addAll(
          Arrays.asList(Objects.requireNonNull(configParams, "configParams is null")));
      return this;
    }

    public RemoteConfigRequest build() {
      RemoteConfigRequest remoteConfigRequest = new RemoteConfigRequest();
      remoteConfigRequest.setConfigKeys(Optional.ofNullable(configKeys).orElse(new String[0]));
      remoteConfigRequest
          .setConfigParams(Optional.ofNullable(configParams).orElse(new ArrayList<>()));
      return remoteConfigRequest;
    }
  }
}
