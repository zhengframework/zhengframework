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

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PackageScanManagedClassProvider implements ManagedClassProvider {

  private final String[] packages;
  private final String[] annotationClasses;

  public PackageScanManagedClassProvider(String... packages) {
    this(new String[] {"javax.persistence.Entity"}, packages);
  }

  public PackageScanManagedClassProvider(String[] annotationClasses, String... packages) {
    this.annotationClasses = Arrays.copyOf(annotationClasses, annotationClasses.length);
    this.packages = Arrays.copyOf(packages, packages.length);
  }

  @Override
  public List<String> get() {
    ScanResult scanResult =
        new ClassGraph()
            .whitelistPackages(packages)
            .enableClassInfo()
            .enableAnnotationInfo()
            .scan();
    ClassInfoList classInfoList =
        scanResult
            .getAllClasses()
            .filter(
                classInfo -> {
                  for (String annotationClass : annotationClasses) {
                    if (classInfo.hasAnnotation(annotationClass)) {
                      return true;
                    }
                  }
                  return false;
                });

    List<String> names = classInfoList.getNames();
    log.info("managedClasses={}", names);
    return names;
  }
}
