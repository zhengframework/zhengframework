package com.github.zhengframework.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

public interface EntityManagerFactoryProvider {

  EntityManagerFactory get(PersistenceUnitInfo persistenceUnitInfo);

}
