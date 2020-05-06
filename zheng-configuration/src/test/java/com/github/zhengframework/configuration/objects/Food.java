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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.ToString;

@ToString
public class Food {

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
    Food food = (Food) o;
    return Objects.equals(getApple(), food.getApple())
        && Objects.equals(getBananas(), food.getBananas())
        && Objects.equals(getApples(), food.getApples());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getApple(), getBananas(), getApples());
  }
}
