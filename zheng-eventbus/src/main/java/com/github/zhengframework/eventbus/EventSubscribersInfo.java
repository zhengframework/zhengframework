package com.github.zhengframework.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriptionIntrospector;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Holds registered event listeners. Will contain nothing if tracking disabled. Service registered
 * in guice and may be injected directly (e.g. for unit tests).
 *
 * @author Vyacheslav Rusakov
 * @since 12.10.2016
 */
@SuppressWarnings({"rawtypes", "UnstableApiUsage"})
@Singleton
public class EventSubscribersInfo {

  private final SubscriptionIntrospector introspect;

  @Inject
  public EventSubscribersInfo(final EventBus eventbus) {
    this.introspect = new SubscriptionIntrospector(eventbus);
  }


  /**
   * May return not just event types, because method could listen for events abstract type or {@link
   * Object} to receive all events.
   *
   * @return set of events with known subscribers or empty set
   */
  public Set<Class> getListenedEvents() {
    return introspect.getListenedEvents();
  }

  /**
   * NOTE: method may return not all listeners, because some methods may listen for a range of
   * events (by base class or {@link Object}). Only direct subscriptions are tracked.
   *
   * @param event event class to get listeners for
   * @return collection of classes listening for event type, or empty list
   */
  public Set<Class> getListenerTypes(final Class<?> event) {
    return introspect.getSubscriberTypes(event);
  }

  /**
   * NOTE: method may return not all listeners, because some methods may listen for a range of
   * events (by base class or {@link Object}). Only direct subscriptions are tracked.
   *
   * @param event event class to get listeners for
   * @return collection of instances listening for event type, or empty list
   */
  public Set<Object> getListeners(final Class<?> event) {
    return introspect.getSubscribers(event);
  }
}
