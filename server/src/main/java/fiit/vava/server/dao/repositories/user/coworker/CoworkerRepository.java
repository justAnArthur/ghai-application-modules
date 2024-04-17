package fiit.vava.server.dao.repositories.user.coworker;

import fiit.vava.server.Coworker;
import fiit.vava.server.dao.repositories.IRepository;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public abstract class CoworkerRepository implements IRepository<Coworker> {

    private static CoworkerRepository instance = null;

    public static synchronized CoworkerRepository getInstance() {
        if (instance == null) {
            Dotenv dotenv = Dotenv.load();

            switch (dotenv.get("REPOSITORY_TYPE")) {
                case "internal":
                    instance = new CoworkerRepositoryInternal();
                    break;
                case "sql":
                    instance = new CoworderRepositorySql();
                    break;
                default:
                    throw new RuntimeException("Unknown repository implementation");
            }
        }

        return instance;
    }

    /*
     * TODO add filtering by coworker
     * NOTE: (mykhailo.sichkaruk@gmail.com) Why this method should be in CoworkerRepo if it is related to Client?
     */
    // public abstract List<Coworker> getNonConfirmedClients();
}
