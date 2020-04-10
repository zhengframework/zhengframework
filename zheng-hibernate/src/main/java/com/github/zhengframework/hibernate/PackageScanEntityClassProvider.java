package com.github.zhengframework.hibernate;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;

/**
 * Recursively locates {@code @Entity} annotated classes in the argument {@link #entityPackages}.
 */
public class PackageScanEntityClassProvider implements HibernateEntityClassProvider {

  private final String[] entityPackages;

  public PackageScanEntityClassProvider(Class<?>... classes) {
    entityPackages = new String[classes.length];
    for (int i = 0; i < classes.length; i++) {
      Class<?> aClass = classes[i];
      entityPackages[i] = aClass.getPackage().getName();
    }
  }

  public PackageScanEntityClassProvider(Package... packages) {
    entityPackages = new String[packages.length];
    for (int i = 0; i < packages.length; i++) {
      Package aPackage = packages[i];
      entityPackages[i] = aPackage.getName();
    }
  }

  public PackageScanEntityClassProvider(final String... entityPackages) {
    this.entityPackages = entityPackages;
  }

  @Override
  public List<Class<?>> get() {
    String entityName = Entity.class.getName();
    final List<Class<?>> entityClasses = new ArrayList<>();
    ScanResult scanResult = new ClassGraph()
        .whitelistPackages(entityPackages)
        .enableClassInfo()
        .enableAnnotationInfo()
        .scan();
    for (io.github.classgraph.ClassInfo classInfo : scanResult
        .getClassesWithAnnotation(entityName)) {
      entityClasses.add(classInfo.loadClass());
    }
    return entityClasses;
  }
}
