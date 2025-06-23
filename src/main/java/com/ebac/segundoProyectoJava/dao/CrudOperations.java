package com.ebac.segundoProyectoJava.dao;

import java.util.List;
import java.util.Optional;

public interface CrudOperations<T>{

    void save(T t);
    void update(T t);
    void delete(T t);
    Optional<T> getById(int id);
    Optional<List<T>> getAll();
}
