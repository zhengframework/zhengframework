package com.github.zhengframework.mybatis;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PackageScanMapperClassProvider implements MapperClassProvider {

  private final String[] packages;

  public PackageScanMapperClassProvider(String... packages) {
    this.packages = packages;
  }

  @Override
  public Collection<Class<?>> get() {
    ScanResult scanResult = new ClassGraph()
        .whitelistPackages(packages)
        .enableClassInfo()
        .scan();
    ClassInfoList allClasses = scanResult.getAllClasses();

    List<Class<?>> loadClasses = allClasses.loadClasses();
    log.info("managedClasses={}", loadClasses);
    return loadClasses;
  }
}
