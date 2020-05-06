package com.github.zhengframework.jpa;

/*-
 * #%L
 * zheng-jpa
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

/**
 * copy from eclipse link project
 * org.eclipse.persistence.internal.jpa.deployment.SEPersistenceUnitInfo
 */
public class PersistenceUnitInfoImpl implements PersistenceUnitInfo {

  // What about 2.0 in 1.0 container here ...
  protected SharedCacheMode cacheMode;
  protected ValidationMode validationMode;
  protected String persistenceUnitName;
  protected String persistenceProviderClassName;
  protected DataSource jtaDataSource;
  protected DataSource nonJtaDataSource;
  protected PersistenceUnitTransactionType persistenceUnitTransactionType;
  protected List<String> mappingFiles;
  protected List<URL> jarFileUrls;
  protected List<String> managedClassNames;
  protected URL persistenceUnitRootUrl;
  protected boolean excludeUnlistedClasses = true;
  protected Properties properties;
  protected ClassLoader tempClassLoader;
  protected ClassLoader realClassLoader;
  // names of jars specified in persistence.xml. they are later on used
  // to build jar-file URL.
  private Collection<String> jarFiles = new ArrayList<String>();

  public PersistenceUnitInfoImpl() {
    mappingFiles = new ArrayList<>();
    managedClassNames = new ArrayList<>();
    properties = new Properties();
    persistenceUnitTransactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;
    // don't initialize jarFileUrls as it is lazily initialized
  }

  /**
   * @return The name of the persistence unit. Corresponds to the &lt;name&gt; element in the
   *     persistence.xml file.
   */
  @Override
  public String getPersistenceUnitName() {
    return persistenceUnitName;
  }

  public void setPersistenceUnitName(String persistenceUnitName) {
    this.persistenceUnitName = persistenceUnitName;
  }

  /**
   * @return The fully qualified name of the persistence provider implementation class. Corresponds
   *     to the &lt;provider&gt; element in the persistence.xml file.
   */
  @Override
  public String getPersistenceProviderClassName() {
    return persistenceProviderClassName;
  }

  public void setPersistenceProviderClassName(String persistenceProviderClassName) {
    this.persistenceProviderClassName = persistenceProviderClassName;
  }

  /**
   * @return The transaction type of the entity managers created by the EntityManagerFactory. The
   *     transaction type corresponds to the transaction-type attribute in the persistence.xml file.
   */
  @Override
  public PersistenceUnitTransactionType getTransactionType() {
    return persistenceUnitTransactionType;
  }

  public void setTransactionType(PersistenceUnitTransactionType persistenceUnitTransactionType) {
    this.persistenceUnitTransactionType = persistenceUnitTransactionType;
  }

  /**
   * @return the JTA-enabled data source to be used by the persistence provider. The data source
   *     corresponds to the &lt;jta-data-source&gt; element in the persistence.xml file or is
   *     provided at deployment or by the container.
   */
  @Override
  public DataSource getJtaDataSource() {
    return jtaDataSource;
  }

  public void setJtaDataSource(DataSource jtaDataSource) {
    this.jtaDataSource = jtaDataSource;
  }

  /**
   * @return The non-JTA-enabled data source to be used by the persistence provider for accessing
   *     data outside a JTA transaction. The data source corresponds to the named
   *     &lt;non-jta-data-source&gt; element in the persistence.xml file or provided at deployment
   *     or by the container.
   */
  @Override
  public DataSource getNonJtaDataSource() {
    return nonJtaDataSource;
  }

  public void setNonJtaDataSource(DataSource nonJtaDataSource) {
    this.nonJtaDataSource = nonJtaDataSource;
  }

  /**
   * @return The list of mapping file names that the persistence provider must load to determine the
   *     mappings for the entity classes. The mapping files must be in the standard XML mapping
   *     format, be uniquely named and be resource-loadable from the application classpath. This
   *     list will not include the orm.xml file if one was specified. Each mapping file name
   *     corresponds to a &lt;mapping-file&gt; element in the persistence.xml file.
   */
  @Override
  public List<String> getMappingFileNames() {
    return mappingFiles;
  }

  public void setMappingFileNames(List<String> mappingFiles) {
    this.mappingFiles = mappingFiles;
  }

  /**
   * @return The list of JAR file URLs that the persistence provider must examine for managed
   *     classes of the persistence unit. Each jar file URL corresponds to a named &lt;jar-file&gt;
   *     element in the persistence.xml file.
   */
  @Override
  public List<URL> getJarFileUrls() {
    if (jarFileUrls == null) { // lazy initialization
      List<URL> jarFileUrls = new ArrayList<URL>(jarFiles.size());
      for (String jarFile : jarFiles) {
        try {
          // build a URL relative to the PU Root
          URL jarFileURL = new URL(persistenceUnitRootUrl, jarFile);
          jarFileUrls.add(jarFileURL);
        } catch (MalformedURLException e) {
          throw new RuntimeException(e);
        }
      }
      synchronized (this) {
        this.jarFileUrls = jarFileUrls;
      }
    }
    return Collections.unmodifiableList(jarFileUrls);
  }

  public void setJarFileUrls(List<URL> jarFileUrls) {
    this.jarFileUrls = jarFileUrls;
  }

  /**
   * @return The URL for the jar file that is the root of the persistence unit. If the persistence
   *     unit is rooted in the WEB-INF/classes directory, this will be the URL of that directory.
   */
  @Override
  public URL getPersistenceUnitRootUrl() {
    return persistenceUnitRootUrl;
  }

  public void setPersistenceUnitRootUrl(URL persistenceUnitRootUrl) {
    this.persistenceUnitRootUrl = persistenceUnitRootUrl;
  }

  /**
   * @return The list of the names of the classes that the persistence provider must add it to its
   *     set of managed classes. Each name corresponds to a named &lt;class&gt; element in the
   *     persistence.xml file.
   */
  @Override
  public List<String> getManagedClassNames() {
    return managedClassNames;
  }

  public void setManagedClassNames(List<String> managedClassNames) {
    this.managedClassNames = managedClassNames;
  }

  /**
   * @return Whether classes in the root of the persistence unit that have not been explicitly
   *     listed are to be included in the set of managed classes. This value corresponds to the
   *     &lt;exclude-unlisted-classes&gt; element in the persistence.xml file.
   */
  @Override
  public boolean excludeUnlistedClasses() {
    return excludeUnlistedClasses;
  }

  public void setExcludeUnlistedClasses(boolean excludeUnlistedClasses) {
    this.excludeUnlistedClasses = excludeUnlistedClasses;
  }

  /**
   * @return Properties object. Each property corresponds to a &lt;property&gt; element in the
   *     persistence.xml file
   */
  @Override
  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  /** @return ClassLoader that the provider may use to load any classes, resources, or open URLs. */
  @Override
  public ClassLoader getClassLoader() {
    return realClassLoader;
  }

  public void setClassLoader(ClassLoader loader) {
    this.realClassLoader = loader;
  }

  /**
   * Add a transformer supplied by the provider that will be called for every new class definition
   * or class redefinition that gets loaded by the loader returned by the
   * PersistenceUnitInfo.getClassLoader method. The transformer has no effect on the result returned
   * by the PersistenceUnitInfo.getNewTempClassLoader method. Classes are only transformed once
   * within the same classloading scope, regardless of how many persistence units they may be a part
   * of.
   *
   * @param transformer A provider-supplied transformer that the Container invokes at
   *     class-(re)definition time
   */
  @Override
  public void addTransformer(ClassTransformer transformer) {
    // not required for our Java SE implementation
  }

  /**
   * Return a ClassLoader that the provider may use to temporarily load any classes, resources, or
   * open URLs. The scope and classpath of this loader is exactly the same as that of the loader
   * returned by PersistenceUnitInfo.getClassLoader. None of the classes loaded by this class loader
   * will be visible to application components.
   *
   * @return Temporary ClassLoader with same visibility as current loader
   */
  @Override
  public ClassLoader getNewTempClassLoader() {
    return tempClassLoader;
  }

  public void setNewTempClassLoader(ClassLoader loader) {
    this.tempClassLoader = loader;
  }

  public Collection<String> getJarFiles() {
    return jarFiles;
  }

  /**
   * @see PersistenceUnitInfo#getPersistenceXMLSchemaVersion()
   * @since Java Persistence 2.0
   */
  @Override
  public String getPersistenceXMLSchemaVersion() {
    //    throw new PersistenceException("Not Yet Implemented");
    return "2.1";
  }

  /**
   * @see PersistenceUnitInfo#getSharedCacheMode()
   * @since Java Persistence 2.0
   */
  @Override
  public SharedCacheMode getSharedCacheMode() {
    return cacheMode;
  }

  /**
   * @see PersistenceUnitInfo#getSharedCacheMode()
   * @since Java Persistence 2.0
   */
  public void setSharedCacheMode(String sharedCacheMode) {
    // If user enters an invalid caching type valueOf will throw an illegal
    // argument exception, e.g.
    // java.lang.IllegalArgumentException: No enum const class
    // javax.persistence.SharedCacheMode.ALLBOGUS
    this.cacheMode = SharedCacheMode.valueOf(sharedCacheMode);
  }

  /**
   * @see PersistenceUnitInfo#getValidationMode()
   * @since Java Persistence 2.0
   */
  @Override
  public ValidationMode getValidationMode() {
    return validationMode;
  }

  /**
   * @see PersistenceUnitInfo#getValidationMode()
   * @since Java Persistence 2.0
   */
  public void setValidationMode(String validationMode) {
    // If user enters an invalid validation mode valueOf will throw an illegal
    // argument exception, e.g.
    // java.lang.IllegalArgumentException: No enum const class
    // javax.persistence.ValidationMode.ALLBOGUS
    this.validationMode = ValidationMode.valueOf(validationMode);
  }
}
