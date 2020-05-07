package com.github.zhengframework.test;

/*-
 * #%L
 * zheng-test
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

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationBuilder;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

@Slf4j
public class ZhengApplicationRunner extends BlockJUnit4ClassRunner {

  private final WithZhengApplicationHolder classHolder;
  private volatile Injector injector;

  public ZhengApplicationRunner(final Class<?> testClass) throws InitializationError {
    super(testClass);
    WithZhengApplication withZhengApplication = testClass.getAnnotation(WithZhengApplication.class);
    if (withZhengApplication != null) {
      classHolder = WithZhengApplicationHolder.create(withZhengApplication);
    } else {
      classHolder = new WithZhengApplicationHolder();
    }
  }

  private Optional<WithZhengApplicationHolder> getWithZhengApplicationFor(
      final FrameworkMethod method) {
    final WithZhengApplication annotation = method.getAnnotation(WithZhengApplication.class);
    return Optional.ofNullable(annotation).map(WithZhengApplicationHolder::create);
  }

  @Override
  protected Object createTest() throws Exception {
    Class<?> javaClass = getTestClass().getJavaClass();

    return injector.getInstance(javaClass);
  }

  private ZhengApplication createZhengApplication(WithZhengApplicationHolder holder)
      throws InitializationError {
    ZhengApplicationBuilder builder = ZhengApplicationBuilder.create();
    if (holder.getConfigurationClass() == null) {
      if (StringUtils.isNotEmpty(holder.getConfigFile())) {
        log.info("use ConfigFile={}", holder.getConfigFile());
        Configuration configuration =
            new ConfigurationBuilder()
                .withConfigurationSource(new FileConfigurationSource(holder.getConfigFile()))
                .build();
        builder.withConfiguration(configuration);
      }
    } else {
      try {
        log.info("user Configuration={}", holder.getConfigurationClass().getName());
        Configuration configuration = holder.getConfigurationClass().getConstructor().newInstance();
        builder.withConfiguration(configuration);
      } catch (InstantiationException
          | IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException e) {
        throw new InitializationError(e);
      }
    }
    builder.withArguments(holder.getArguments());
    builder.withAutoLoadModule(holder.isAutoLoadModule());
    List<Module> moduleList = new ArrayList<>(holder.getModuleClass().length);
    for (Class<? extends Module> moduleClass : holder.getModuleClass()) {
      try {
        moduleList.add(moduleClass.getConstructor().newInstance());
      } catch (InstantiationException
          | IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException e) {
        log.error("create ZhengApplication fail, holder={}", holder, e);
        throw new InitializationError(e);
      }
    }
    builder.addModule(moduleList);
    builder.excludeModule(holder.getExcludeModuleClass());
    builder.excludeModuleProvider(holder.getExcludeModuleProviderClass());
    return builder.build();
  }

  @SneakyThrows
  @Override
  protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
    WithZhengApplicationHolder current =
        classHolder.merge(
            getWithZhengApplicationFor(method).orElse(new WithZhengApplicationHolder()));
    ZhengApplication application = createZhengApplication(current);
    application.start();
    injector = application.getInjector();
    super.runChild(method, notifier);
    application.stop();
  }
}
