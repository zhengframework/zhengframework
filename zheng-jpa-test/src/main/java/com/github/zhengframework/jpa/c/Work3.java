package com.github.zhengframework.hibernate.c;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

public class Work3 {

  private final EntityManager entityManager;

  @Inject
  public Work3(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Transactional
  public void makeAThing() {
    entityManager.persist(new Thing3("Thing " + Math.random()));
  }

  @Transactional
  public long countThings() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    cq.select(cb.count(cq.from(Thing3.class)));
    return entityManager.createQuery(cq).getSingleResult();
  }
}
