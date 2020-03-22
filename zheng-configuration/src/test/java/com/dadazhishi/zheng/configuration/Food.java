package com.dadazhishi.zheng.configuration;

import java.util.List;
import java.util.Map;

public class Food {

  private Apple apple;

  private List<Banana> bananas;

  private Map<String,Apple> apples;

  public Apple getApple() {
    return apple;
  }

  public void setApple(Apple apple) {
    this.apple = apple;
  }

  public List<Banana> getBananas() {
    return bananas;
  }

  public void setBananas(List<Banana> bananas) {
    this.bananas = bananas;
  }

  public Map<String, Apple> getApples() {
    return apples;
  }

  public void setApples(Map<String, Apple> apples) {
    this.apples = apples;
  }
}
