package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationDefineModuleTest {

  Food food;

  @Before
  public void setup() {
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
  }

  @Test
  public void testAnnotation() throws IOException {
    PropertiesConfigurationSource propertiesConfigurationSource = new PropertiesConfigurationSource
        (PropertiesConfigurationSourceTest.class.getResourceAsStream("/food.properties"));

    Configuration configuration = propertiesConfigurationSource.read();

    ConfigurationModule configurationModule = new ConfigurationModule(configuration);
    configurationModule.setConfigurationPackages("com.dadazhishi.zheng.configuration");
    Injector injector = Guice
        .createInjector(configurationModule, new FoodModule());
    FoodAnnotation food2 = injector.getInstance(FoodAnnotation.class);
    assertEquals(food.getApple(), food2.getApple());
    System.out.println(food2.getApple());
    Set<BananaAnnotation> bananaAnnotationList = injector
        .getInstance(Key.get(new TypeLiteral<Set<BananaAnnotation>>() {
        }));
    assertEquals(food.getBananas().size(), bananaAnnotationList.size());
    Map<String, AppleAnnotation> appleAnnotationMap = injector
        .getInstance(Key.get(new TypeLiteral<Map<String, AppleAnnotation>>() {
        }));
    assertEquals(food.getApples().size(), appleAnnotationMap.size());
  }


  @Test
  public void configure() throws IOException {
    PropertiesConfigurationSource propertiesConfigurationSource = new PropertiesConfigurationSource
        (PropertiesConfigurationSourceTest.class.getResourceAsStream("/food.properties"));

    Configuration configuration = propertiesConfigurationSource.read();

    Injector injector = Guice
        .createInjector(new ConfigurationModule(configuration), new FoodModule());
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
    Food food(Configuration configuration, ConfigurationMapper configurationMapper) {
      return configurationMapper.resolve(configuration, Food.class);
    }

    @Provides
    Apple apple(Configuration configuration, ConfigurationMapper configurationMapper) {
      Configuration configuration1 = configuration.getConfiguration("apple");
      return configurationMapper.resolve(configuration1, Apple.class);
    }

  }
}