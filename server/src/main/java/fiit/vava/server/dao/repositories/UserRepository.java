package fiit.vava.server.dao.repositories;

import fiit.vava.server.User;
import fiit.vava.server.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IRepository<User> {

    private static UserRepository instance = null;

    private UserRepository() {
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();

        return instance;
    }


    private final ArrayList<User> users = new ArrayList<>() {{
        add(User.newBuilder().setEmail("first@first.first").setPassword("first").setRole(UserRole.CLIENT).setConfirmed(true).build());
        add(User.newBuilder().setEmail("second@second.second").setPassword("second").setRole(UserRole.COWORKER).build());
        add(User.newBuilder().setEmail("third@third.third").setPassword("third").setRole(UserRole.ADMIN).build());
    }};

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User save(User toSave) {
        users.add(toSave);
        return toSave;
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
