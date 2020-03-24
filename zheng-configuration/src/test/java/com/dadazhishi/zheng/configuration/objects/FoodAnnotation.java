package com.dadazhishi.zheng.configuration.objects;

import com.dadazhishi.zheng.configuration.annotation.ConfigurationDefine;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ConfigurationDefine
public class FoodAnnotation {

  private Apple apple;

  private List<Banana> bananas;

  private Map<String, Apple> apples;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FoodAnnotation food = (FoodAnnotation) o;
    return Objects.equals(getApple(), food.getApple()) &&
        Objects.equals(getBananas(), food.getBananas()) &&
        Objects.equals(getApples(), food.getApples());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getApple(), getBananas(), getApples());
  }
}
