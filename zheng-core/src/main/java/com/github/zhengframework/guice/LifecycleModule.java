package com.github.zhengframework.guice;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


public class LifecycleModule extends AbstractModule {

  private Matcher<Object> typeMatcher;

  public LifecycleModule() {
    this(Matchers.any());
  }

  public LifecycleModule(final String pkg) {
    this(new ObjectPackageMatcher<>(pkg));
  }

  public LifecycleModule(final Matcher<Object> typeMatcher) {
    this.typeMatcher = typeMatcher;
  }

  @Override
  protected void configure() {
    DestroyableManager manager = new DestroyableManager();
    bind(DestroyableManager.class).toInstance(manager);

    bindListener(typeMatcher, new GeneralTypeListener<>(
        Destroyable.class, new DestroyableTypeProcessor(manager)));

    bindListener(typeMatcher, new AnnotatedMethodTypeListener<>(
        PostConstruct.class, new PostConstructAnnotationProcessor()));

    bindListener(typeMatcher, new AnnotatedMethodTypeListener<>(
        PreDestroy.class, new PreDestroyAnnotationProcessor(manager)));

  }
}
