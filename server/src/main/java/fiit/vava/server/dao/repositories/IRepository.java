package fiit.vava.server.dao.repositories;

import java.util.List;

public interface IRepository<T> {

    T save(T toSave);

    List<T> findAll();

    T findById(String id);
}
