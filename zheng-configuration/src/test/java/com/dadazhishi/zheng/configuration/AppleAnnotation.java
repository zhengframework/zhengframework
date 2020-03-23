package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.annotation.Configuration;
import com.dadazhishi.zheng.configuration.annotation.ConfigurationType;
import java.util.Objects;

@Configuration(namespace = "apples", type = ConfigurationType.MAP)
public class AppleAnnotation {

  private String name;
  private boolean big;
  private double weight;

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isBig() {
    return big;
  }

  public void setBig(boolean big) {
    this.big = big;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppleAnnotation apple = (AppleAnnotation) o;
    return isBig() == apple.isBig() &&
        Double.compare(apple.getWeight(), getWeight()) == 0 &&
        Objects.equals(getName(), apple.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), isBig(), getWeight());
  }

  @Override
  public String toString() {
    return "Apple{" +
        "name='" + name + '\'' +
        ", big=" + big +
        ", weight=" + weight +
        '}';
  }
}
