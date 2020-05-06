package com.github.zhengframework.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.github.zhengframework.event.EventModuleTest.MyModule;
import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
@WithZhengApplication(moduleClass = {MyModule.class})
public class EventModuleTest {

  @Inject private Injector injector;

  @Test
  public void testProvidedComponentsPresent() {
    EventDispatcher dispatcher = injector.getInstance(EventDispatcher.class);
    TestAnnotatedListener listener = injector.getInstance(TestAnnotatedListener.class);
    TestListenerInterface listenerInterface = injector.getInstance(TestListenerInterface.class);
    assertNotNull(dispatcher);
    assertNotNull(listener);
    assertNotNull(listenerInterface);
  }

  @Test
  public void testAnnotatedListener() throws Exception {
    EventDispatcher dispatcher = injector.getInstance(EventDispatcher.class);
    TestAnnotatedListener listener = injector.getInstance(TestAnnotatedListener.class);
    assertEquals(0, listener.invocationCount.get());
    dispatcher.publishEvent(new TestEvent());
    assertEquals(1, listener.invocationCount.get());
    dispatcher.publishEvent(new NotTestEvent());
    assertEquals(1, listener.invocationCount.get());
  }

  @Test
  public void testManuallyRegisteredEventListeners() throws Exception {
    EventDispatcher dispatcher = injector.getInstance(EventDispatcher.class);
    final AtomicInteger testEventCounter = new AtomicInteger();
    final AtomicInteger notTestEventCounter = new AtomicInteger();
    final AtomicInteger allEventCounter = new AtomicInteger();

    dispatcher.registerListener(
        TestEvent.class,
        new EventListener<TestEvent>() {
          public void onEvent(TestEvent event) {
            testEventCounter.incrementAndGet();
          }
        });
    dispatcher.registerListener(
        NotTestEvent.class,
        new EventListener<NotTestEvent>() {
          public void onEvent(NotTestEvent event) {
            notTestEventCounter.incrementAndGet();
          }
        });
    dispatcher.registerListener(
        Event.class,
        new EventListener<Event>() {
          public void onEvent(Event event) {
            allEventCounter.incrementAndGet();
          }
        });

    dispatcher.publishEvent(new TestEvent());
    assertEquals(1, testEventCounter.get());
    assertEquals(0, notTestEventCounter.get());
    assertEquals(1, allEventCounter.get());
  }

  @Test
  public void testManuallyRegisteredEventListenersWithoutClassArgument() throws Exception {
    EventDispatcher dispatcher = injector.getInstance(EventDispatcher.class);
    final AtomicInteger testEventCounter = new AtomicInteger();
    final AtomicInteger notTestEventCounter = new AtomicInteger();
    final AtomicInteger allEventCounter = new AtomicInteger();

    dispatcher.registerListener(
        new EventListener<TestEvent>() {
          public void onEvent(TestEvent event) {
            testEventCounter.incrementAndGet();
          }
        });
    dispatcher.registerListener(
        new EventListener<NotTestEvent>() {
          public void onEvent(NotTestEvent event) {
            notTestEventCounter.incrementAndGet();
          }
        });
    dispatcher.registerListener(
        new EventListener<Event>() {
          public void onEvent(Event event) {
            allEventCounter.incrementAndGet();
          }
        });

    dispatcher.publishEvent(new TestEvent());
    assertEquals(1, testEventCounter.get());
    assertEquals(0, notTestEventCounter.get());
    assertEquals(1, allEventCounter.get());
  }

  @Test
  public void testInjectorDiscoveredEventListeners() throws Exception {
    EventDispatcher dispatcher = injector.getInstance(EventDispatcher.class);
    TestListenerInterface listener = injector.getInstance(TestListenerInterface.class);
    assertEquals(0, listener.invocationCount.get());
    dispatcher.publishEvent(new TestEvent());
    assertEquals(1, listener.invocationCount.get());
    dispatcher.publishEvent(new NotTestEvent());
    assertEquals(1, listener.invocationCount.get());
  }

  @Test
  public void testUnregisterEventListener() throws Exception {
    EventDispatcher dispatcher = injector.getInstance(EventDispatcher.class);
    final AtomicInteger testEventCounter = new AtomicInteger();

    EventRegistration registration =
        dispatcher.registerListener(
            new EventListener<TestEvent>() {
              public void onEvent(TestEvent event) {
                testEventCounter.incrementAndGet();
              }
            });

    dispatcher.publishEvent(new TestEvent());
    assertEquals(1, testEventCounter.get());
    registration.unregister();
    assertEquals(1, testEventCounter.get());
  }

  public static class MyModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(TestAnnotatedListener.class).toInstance(new TestAnnotatedListener());
      bind(TestListenerInterface.class).toInstance(new TestListenerInterface());
    }
  }

  public static class TestAnnotatedListener {

    AtomicInteger invocationCount = new AtomicInteger();

    @EventSubscribe
    public void doThing(TestEvent event) {
      invocationCount.incrementAndGet();
    }
  }

  public static class TestFailFastEventListener {

    @EventSubscribe
    public void doNothing(String invalidArgumentType) {
      fail("This should never be called");
    }
  }

  public static class TestListenerInterface implements EventListener<TestEvent> {

    AtomicInteger invocationCount = new AtomicInteger();

    @Override
    public void onEvent(TestEvent event) {
      invocationCount.incrementAndGet();
    }
  }

  private class TestEvent implements Event {}

  private class NotTestEvent implements Event {}
}
