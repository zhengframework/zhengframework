package com.github.zhengframework.eventbus;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Data
public class PubEvent {

  private String hello = UUID.randomUUID().toString();
}
