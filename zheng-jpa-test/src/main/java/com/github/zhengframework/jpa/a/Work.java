package com.github.zhengframework.jpa.a;

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
