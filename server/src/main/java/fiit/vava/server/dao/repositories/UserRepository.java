package fiit.vava.server.dao.repositories;

import fiit.vava.server.User;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Optional;

public abstract class UserRepository implements IRepository<User> {
    private static UserRepository instance = null;

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            Dotenv dotenv = Dotenv.load();

            switch (dotenv.get("REPOSITORY_TYPE")) {
                case "internal":
                    instance = new UserRepositoryInternal();
                    break;
                case "sql":
                    instance = new UserRepositorySql();
                    break;
                default:
                    throw new RuntimeException("Unknown repository implementation");
            }
        }

        return instance;
    }

    public abstract Optional<User> findByEmail(String email);

    public abstract boolean setConfirmed(User requested);
}