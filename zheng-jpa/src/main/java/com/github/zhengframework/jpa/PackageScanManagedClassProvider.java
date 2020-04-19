package com.github.zhengframework.jpa;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ClassInfoList.ClassInfoFilter;
import io.github.classgraph.ScanResult;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PackageScanManagedClassProvider implements ManagedClassProvider {

  private final String[] packages;
  private final String[] annotationClasses;

  public PackageScanManagedClassProvider(String... packages) {
    this(new String[]{"javax.persistence.Entity"}, packages);
  }

  public PackageScanManagedClassProvider(String[] annotationClasses, String... packages) {
    this.annotationClasses = annotationClasses;
    this.packages = packages;
  }


  @Override
  public List<String> get() {
    ScanResult scanResult = new ClassGraph()
        .whitelistPackages(packages)
        .enableClassInfo()
        .enableAnnotationInfo()
        .scan();
    ClassInfoList classInfoList = scanResult.getAllClasses().filter(new ClassInfoFilter() {
      @Override
      public boolean accept(ClassInfo classInfo) {
        for (String annotationClass : annotationClasses) {
          if (classInfo.hasAnnotation(annotationClass)) {
            return true;
          }
        }
        return false;
      }
    });

    List<String> names = classInfoList.getNames();
    log.info("managedClasses={}", names);
    return names;
  }
}
