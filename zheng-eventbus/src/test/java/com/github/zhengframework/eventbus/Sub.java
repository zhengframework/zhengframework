package com.github.zhengframework.eventbus;

import com.google.common.eventbus.Subscribe;
import javax.inject.Singleton;

@Singleton
public class Sub {

  private int count = 0;

  public int getCount() {
    return count;
  }

  @Subscribe
  public void getEvent(PubEvent pubEvent) {
    System.out.println(pubEvent);
    count++;
  }
}
