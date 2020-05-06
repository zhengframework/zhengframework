package com.github.zhengframework.configuration.objects;

/*-
 * #%L
 * zheng-configuration
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Objects;

public class Apple {

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
    Apple apple = (Apple) o;
    return isBig() == apple.isBig()
        && Double.compare(apple.getWeight(), getWeight()) == 0
        && Objects.equals(getName(), apple.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), isBig(), getWeight());
  }

  @Override
  public String toString() {
    return "Apple{" + "name='" + name + '\'' + ", big=" + big + ", weight=" + weight + '}';
  }
}
