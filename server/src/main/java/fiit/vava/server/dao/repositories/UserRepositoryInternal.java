package fiit.vava.server.dao.repositories;

import fiit.vava.server.User;
import fiit.vava.server.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryInternal extends UserRepository {

    private final ArrayList<User> users = new ArrayList<>() {{
        add(User.newBuilder().setId("1").setEmail("first@first.first").setPassword("first").setRole(UserRole.CLIENT).build());
        add(User.newBuilder().setId("2").setEmail("second@second.second").setPassword("second").setRole(UserRole.COWORKER).build());
        add(User.newBuilder().setId("3").setEmail("third@third.third").setPassword("third").setRole(UserRole.ADMIN).build());
    }};

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User findById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User save(User toSave) {
        if (toSave.getId() != null)
            users.removeIf(user -> user.getId().equals(toSave.getId()));

        users.add(toSave);
        return toSave;
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public boolean setConfirmed(User requested) {
        Optional<User> userOptional = users.stream()
                .filter(_user -> _user.getId().equals(requested.getId()))
                .findFirst();

        if (userOptional.isEmpty())
            return false;

        User user = userOptional.get();

        user.toBuilder().setConfirmed(true);
        this.save(user);

        return true;
    }
}
