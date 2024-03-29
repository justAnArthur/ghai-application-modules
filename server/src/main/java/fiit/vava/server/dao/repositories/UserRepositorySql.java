package fiit.vava.server.dao.repositories;

import fiit.vava.server.User;

import java.util.List;
import java.util.Optional;

public class UserRepositorySql extends UserRepository {
    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(String id) {
        return null;
    }

    @Override
    public User save(User toSave) {
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public boolean setConfirmed(User requested) {
        return false;
    }
}
