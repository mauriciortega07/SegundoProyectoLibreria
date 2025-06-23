package com.ebac.segundoProyectoJava.dao;

import com.ebac.segundoProyectoJava.exceptions.ExcepcionesPersonalizadas;
import com.ebac.segundoProyectoJava.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;


public class Library<T> implements CrudOperations<T> {
    private final Class<T> tipoObjeto;
    private final EntityManager entityManager;

    public Library(Class<T> tipoObejto, EntityManager entityManager) {
        this.tipoObjeto = tipoObejto;
        this.entityManager = entityManager;
    }

    @Override
    public void save(T t) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(t);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new ExcepcionesPersonalizadas("Error al guardar el registro", e);
        }

    }

    @Override
    public void update(T t) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(t);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new ExcepcionesPersonalizadas("Error al ejecutar el UPDATE : ", e);
        }

    }

    @Override
    public void delete(T t) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            T entidadManejada = entityManager.contains(t) ? t : entityManager.merge(t);
            entityManager.remove(entidadManejada);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new ExcepcionesPersonalizadas("Error al ejecutar el DELETE : ", e);
        }

    }

    @Override
    public Optional<T> getById(int id) {
        try {
            T registro = entityManager.find(tipoObjeto, id);
            return Optional.ofNullable(registro);
        } catch (Exception e) {
            System.out.println("No se pudo encontrar el registro");
            //throw new ExcepcionesPersonalizadas("No se pudo encontrar el registro", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<T>> getAll() {
        try {
            String sqlString = "SELECT e FROM " + tipoObjeto.getSimpleName() + " e";

            return Optional.of(entityManager.createQuery(sqlString, tipoObjeto).getResultList());
        } catch (Exception e) {
            throw new ExcepcionesPersonalizadas("Error en obtener todos los registros : ", e);
        }

    }

    public Optional<Book> getByISBN(String isbn) {
        try {
            String jpql = "SELECT b FROM " + tipoObjeto.getSimpleName() + " b WHERE b.isbnCode = :isbn";
            Book book = entityManager.createQuery(jpql, Book.class).setParameter("isbn", isbn).getSingleResult();
            return Optional.of(book);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new ExcepcionesPersonalizadas("Error al obtener por ISBN", e);
        }
    }
}
