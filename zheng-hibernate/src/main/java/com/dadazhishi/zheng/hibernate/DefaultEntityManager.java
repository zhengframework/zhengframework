package com.dadazhishi.zheng.hibernate;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

public class DefaultEntityManager {

  @Inject
  static Provider<EntityManager> entityManagerProvider;

  public static EntityManager get() {
    return entityManagerProvider.get();
  }

}
