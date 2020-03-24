package com.dadazhishi.zheng.configuration.objects;

import com.dadazhishi.zheng.configuration.annotation.ConfigurationDefine;
import com.dadazhishi.zheng.configuration.annotation.ConfigurationType;
import java.util.Objects;

@ConfigurationDefine(namespace = "bananas", type = ConfigurationType.SET)
public class BananaAnnotation {

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
    BananaAnnotation banana = (BananaAnnotation) o;
    return getColor() == banana.getColor() &&
        Objects.equals(getName(), banana.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getColor());
  }
}
