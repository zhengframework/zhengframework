package com.dadazhishi.zheng.configuration.objects;

import java.util.Objects;

public class Banana {

  private String name;

  private int color;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Banana banana = (Banana) o;
    return getColor() == banana.getColor() &&
        Objects.equals(getName(), banana.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getColor());
  }
}
