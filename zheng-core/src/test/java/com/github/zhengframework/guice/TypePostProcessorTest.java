package com.github.zhengframework.guice;

import com.google.inject.AbstractModule;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import com.google.inject.matcher.Matchers;
import javax.inject.Singleton;
import org.junit.Assert;
import org.junit.Test;

public class TypePostProcessorTest {

  @Test
  public void testPostProcessingByType() throws Exception {
    final PostProcessor postProcessor = new PostProcessor();
    Injector injector =
        Guice.createInjector(
            new AbstractModule() {
              @Override
              protected void configure() {
                bind(Bean1.class).asEagerSingleton();
                bind(Bean2.class).asEagerSingleton();
                bind(Bean3.class).asEagerSingleton();
                bindListener(
                    Matchers.any(),
                    new GeneralTypeListener<AbstractBean>(AbstractBean.class, postProcessor));
              }
            });
    Assert.assertEquals(postProcessor.called, 2);
    Assert.assertEquals(injector.getInstance(Bean1.class).called, 1);
    Assert.assertEquals(injector.getInstance(Bean2.class).called, 1);
  }

  @Test(expected = CreationException.class)
  public void testPostProcessingFailure() throws Exception {
    Guice.createInjector(
        new AbstractModule() {
          @Override
          protected void configure() {
            bind(ExceptionalBean.class).asEagerSingleton();
            bindListener(
                Matchers.any(),
                new GeneralTypeListener<AbstractBean>(AbstractBean.class, new PostProcessor()));
          }
        });
  }

  @Test(expected = ProvisionException.class)
  public void testPostProcessingFailure2() throws Exception {
    Guice.createInjector(
        new AbstractModule() {
          @Override
          protected void configure() {
            bind(ExceptionalBean.class).in(Singleton.class);
            bindListener(
                Matchers.any(),
                new GeneralTypeListener<AbstractBean>(AbstractBean.class, new PostProcessor()));
          }
        })
        .getInstance(ExceptionalBean.class);
  }

  public abstract static class AbstractBean {

    public int called;

    public void call() {
      called++;
    }
  }

  public static class Bean1 extends AbstractBean {

  }

  public static class Bean2 extends AbstractBean {

  }

  public static class Bean3 {

  }

  public static class ExceptionalBean extends AbstractBean {

    @Override
    public void call() {
      throw new IllegalStateException("Bad");
    }
  }

  public static class PostProcessor implements TypePostProcessor<AbstractBean> {

    public int called;

    @Override
    public void process(AbstractBean instance) throws Exception {
      instance.call();
      called++;
    }
  }
}
