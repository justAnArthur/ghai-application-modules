package fiit.vava.server.dao.repositories;

public interface IRepository<T> {
    T[] findAll();
}
