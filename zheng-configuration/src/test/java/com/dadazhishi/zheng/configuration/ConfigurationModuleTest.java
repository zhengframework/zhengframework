package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;

import com.dadazhishi.zheng.configuration.objects.Apple;
import com.dadazhishi.zheng.configuration.objects.Banana;
import com.dadazhishi.zheng.configuration.objects.Food;
import com.dadazhishi.zheng.configuration.objects.NamedAnnotation;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationModuleTest {

  Food food;
  Injector injector;
  Configuration configuration;

  @Before
  public void setup() throws IOException {
    food = new Food();
    Apple apple = new Apple();
    apple.setBig(true);
    apple.setWeight(1.9);
    apple.setName("110");
    food.setApple(apple);
    food.setApples(Collections.singletonMap("abc", apple));
    List<Banana> list = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Banana banana = new Banana();
      banana.setName("name" + i);
      banana.setColor(i * 100);
      list.add(banana);
    }
    food.setBananas(list);

    configuration = ConfigurationBuilder.create()
        .withEnvironmentVariables()
        .withSystemProperties()
        .withClassPathProperties("food.properties")
        .build();

    ConfigurationModule configurationModule = new ConfigurationModule();
    configurationModule.setConfiguration(configuration);
    injector = Guice
        .createInjector(configurationModule, new FoodModule());
  }

  @Test
  public void testNamedAnnotation() {
    System.out.println(injector.getInstance(NamedAnnotation.class).getName());
    System.out.println(injector.getInstance(NamedAnnotation.class).getName());
  }

  @Test
  public void testGetMapKeyNamed() {
    Configuration configuration = injector.getInstance(Configuration.class);
    Map<String, Apple> apples = ConfigurationObjectMapper
        .resolveMap(configuration, "apples", Apple.class);
    Apple abc = apples.get("abc");
    System.out.println(abc.getWeight());
    System.out.println(abc.getName());
  }

  @Test
  public void testAnnotation() {

    Configuration configuration = injector.getInstance(Configuration.class);
    Food food2 = ConfigurationObjectMapper.resolve(configuration, Food.class);
    assertEquals(food.getApple(), food2.getApple());
    System.out.println(food2.getApple());

    Set<Banana> bananas = ConfigurationObjectMapper
        .resolveSet(configuration, "bananas", Banana.class);

    assertEquals(food.getBananas().size(), bananas.size());

    Map<String, Apple> apples = ConfigurationObjectMapper
        .resolveMap(configuration, "apples", Apple.class);

    assertEquals(food.getApples().size(), apples.size());
  }


  @Test
  public void testStatic() throws IOException {
    Food food2 = injector.getInstance(Food.class);
    assertEquals(food, food2);

    Apple apple = injector.getInstance(Apple.class);
    assertEquals(food.getApple(), apple);

  }

  public static class FoodModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    Food food(Configuration configuration) {
      return ConfigurationObjectMapper.resolve(configuration, Food.class);
    }

    @Provides
    Apple apple(Configuration configuration) {
      Configuration configuration1 = configuration.getConfiguration("apple");
      return ConfigurationObjectMapper.resolve(configuration1, Apple.class);
    }

  }
}