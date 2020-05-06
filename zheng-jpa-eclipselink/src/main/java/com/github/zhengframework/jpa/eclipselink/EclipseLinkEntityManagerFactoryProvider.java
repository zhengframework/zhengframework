package com.github.zhengframework.jpa.eclipselink;

/*-
 * #%L
 * zheng-jpa-eclipselink
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
      // fake root url
      persistenceUnitInfo1.setPersistenceUnitRootUrl(new URL("http://localhost/"));
      return new PersistenceProvider()
          .createContainerEntityManagerFactory(persistenceUnitInfo1, null);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
