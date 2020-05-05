package com.github.zhengframework.mybatis;

/*-
 * #%L
 * zheng-mybatis
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
    ScanResult scanResult = new ClassGraph().whitelistPackages(packages).enableClassInfo().scan();
    ClassInfoList allClasses = scanResult.getAllClasses();

    List<Class<?>> loadClasses = allClasses.loadClasses();
    log.info("managedClasses={}", loadClasses);
    return loadClasses;
  }
}
