package fiit.vava.server.dao.repositories.user.client;

import fiit.vava.server.Client;
import fiit.vava.server.dao.repositories.IRepository;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public abstract class ClientRepository implements IRepository<Client> {

    private static ClientRepository instance = null;

    public static synchronized ClientRepository getInstance() {
        if (instance == null) {
            Dotenv dotenv = Dotenv.load();

            switch (dotenv.get("REPOSITORY_TYPE")) {
                case "internal":
                    instance = new ClientRepositoryInternal();
                    break;
                case "sql":
                    instance = new ClientRepositorySql();
                    break;
                default:
                    throw new RuntimeException("Unknown repository implementation");
            }
        }

        return instance;
    }

    /*
     * TODO add filtering by coworker
     */
    public abstract List<Client> getNonConfirmedClients();

    public abstract Client findByUserId(String id);
}
