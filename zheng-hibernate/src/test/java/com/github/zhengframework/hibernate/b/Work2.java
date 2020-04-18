package com.github.zhengframework.hibernate.b;

import com.google.inject.persist.Transactional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class Work2 {

  private final EntityManager entityManager;

  @Inject
  public Work2(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Transactional
  public void makeAThing() {
    entityManager.persist(new Thing2("Thing " + Math.random()));
  }

  @Transactional
  public long countThings() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    cq.select(cb.count(cq.from(Thing2.class)));
    return entityManager.createQuery(cq).getSingleResult();
  }
}
