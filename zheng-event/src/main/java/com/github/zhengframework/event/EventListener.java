package com.github.zhengframework.event;

public interface EventListener<T extends Event> {

  void onEvent(T event);

}
