package com.github.zhengframework.jpa.a;

/*-
 * #%L
 * zheng-jpa-test
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

import com.google.inject.persist.Transactional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class Work {

  private final EntityManager entityManager;

  @Inject
  public Work(EntityManager entityManager) {

    this.entityManager = entityManager;
  }

  @Transactional
  public void makeAThing() {
    entityManager.persist(new Thing("Thing " + Math.random()));
  }

  @Transactional
  public long countThings() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    cq.select(cb.count(cq.from(Thing.class)));
    return entityManager.createQuery(cq).getSingleResult();
  }
}
