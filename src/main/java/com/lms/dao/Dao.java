package com.lms.dao;

import java.util.List;

/** Generic DAO contract (interface + generics). */
public interface Dao<T> {
    T findById(int id);
    List<T> findAll();
    boolean delete(int id);
}
