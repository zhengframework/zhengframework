package com.github.zhengframework.event;

import com.google.inject.Provider;
import com.google.inject.spi.ProvisionListener;

class EventSubscribingProvisionListener implements ProvisionListener {

  private final Provider<EventDispatcher> dispatcherProvider;

  EventSubscribingProvisionListener(
      Provider<EventDispatcher> dispatcherProvider) {
    this.dispatcherProvider = dispatcherProvider;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> void onProvision(ProvisionInvocation<T> provision) {
    T provisioned = provision.provision();
    if (provisioned instanceof EventListener) {
      dispatcherProvider.get().registerListener((EventListener) provisioned);
    }
  }
}
