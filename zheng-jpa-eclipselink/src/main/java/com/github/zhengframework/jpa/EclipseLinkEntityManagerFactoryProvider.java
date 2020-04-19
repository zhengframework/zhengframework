package com.github.zhengframework.jpa;

import java.net.MalformedURLException;
import java.net.URL;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import org.eclipse.persistence.jpa.PersistenceProvider;

public class EclipseLinkEntityManagerFactoryProvider implements EntityManagerFactoryProvider {

  @Override
  public EntityManagerFactory get(PersistenceUnitInfo persistenceUnitInfo) {
    try {
      PersistenceUnitInfoImpl persistenceUnitInfo1 = (PersistenceUnitInfoImpl) persistenceUnitInfo;
      //fake root url
      persistenceUnitInfo1
          .setPersistenceUnitRootUrl(new URL("http://localhost/"));
      return new PersistenceProvider()
          .createContainerEntityManagerFactory(persistenceUnitInfo1, null);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
