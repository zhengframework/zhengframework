package com.github.zhengframework.jpa.eclipselink;

import com.github.zhengframework.jpa.EntityManagerFactoryProvider;
import com.github.zhengframework.jpa.PersistenceUnitInfoImpl;
import java.net.MalformedURLException;
import java.net.URL;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.kohsuke.MetaInfServices;

@MetaInfServices
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
