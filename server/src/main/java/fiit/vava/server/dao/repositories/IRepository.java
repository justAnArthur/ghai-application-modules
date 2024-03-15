package fiit.vava.server.dao.repositories;

import java.util.List;

public interface IRepository<T> {

    List<T> findAll();

    T save(T toSave);
}
