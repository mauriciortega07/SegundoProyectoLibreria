package com.ebac.segundoProyectoJava.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MySqlConnection {
    public static EntityManager getConnection(){
        try {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("connectionMySQL");
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            return entityManager;
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con la BD", e);
        }
    }
}
