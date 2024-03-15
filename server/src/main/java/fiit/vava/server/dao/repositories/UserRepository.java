package fiit.vava.server.dao.repositories;

import fiit.vava.server.User;
import fiit.vava.server.UserRole;

import java.util.Arrays;
import java.util.Optional;

public class UserRepository implements IRepository<User> {

    private static UserRepository instance = null;

    public UserRepository() {
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();

        return instance;
    }

    private final User[] users = new User[]{
            User.newBuilder().setEmail("first@first.first").setPassword("first").setRole(UserRole.CLIENT).build(),
            User.newBuilder().setEmail("second@second.second").setPassword("second").setRole(UserRole.COWORKER).build(),
            User.newBuilder().setEmail("third@third.third").setPassword("third").setRole(UserRole.ADMIN).build(),
    };

    @Override
    public User[] findAll() {
        return users;
    }

    public Optional<User> findByEmail(String email) {
        return Arrays.stream(users)
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
