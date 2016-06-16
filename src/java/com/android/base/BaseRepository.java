package com.android.base;

import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class BaseRepository<E extends BaseEntity> {

    private String Select = "select %s from %s as %s";
    protected EntityManager entityManager;
    protected Class<E> entityClass;

    public BaseRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
        entityManager = PersistenceUtilities.getFactory().createEntityManager();
    }

    public void close() {
        entityManager.close();
    }

    public void persist(E entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    public void merge(E entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    public void remove(long entityId) {
        E entity = entityManager.getReference(entityClass, entityId);
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }

    public E find(long entityId) {
        return entityManager.getReference(entityClass, entityId);
    }

    public String createSelectSQL() {
        String entityName = entityClass.getSimpleName();
        String variableName = entityName.toLowerCase(Locale.US);
        String jpql = String.format(Select, variableName, entityName, variableName);
        return jpql;
    }

    public List<E> list() {
        String qL = createSelectSQL();
        Query query = entityManager.createQuery(qL);
        return query.getResultList();
    }

}
