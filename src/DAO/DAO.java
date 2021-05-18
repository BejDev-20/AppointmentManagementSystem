package DAO;

import javafx.collections.ObservableList;

/**
 * Represents an interface for the DAO pattern, provides getAll, add, update, and delete skeleton for the classes.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public interface DAO<T> {

    public ObservableList<T> getAll();
    public T getById(int id);
    public boolean add(T item);
    public boolean update(T item);
    public boolean delete(T item);
}
