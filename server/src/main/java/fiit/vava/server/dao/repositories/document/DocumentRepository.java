package fiit.vava.server.dao.repositories.document;

import fiit.vava.server.Document;
import fiit.vava.server.dao.repositories.IRepository;
import io.github.cdimascio.dotenv.Dotenv;

public abstract class DocumentRepository implements IRepository<Document> {

    private static DocumentRepository instance = null;

    public static synchronized DocumentRepository getInstance() {
        if (instance == null) {
            Dotenv dotenv = Dotenv.load();

            switch (dotenv.get("REPOSITORY_TYPE")) {
                case "internal":
                    instance = new DocumentRepositoryInternal();
                    break;
                case "sql":
                    // instance = new DocumentRequestRepositorySql();
                    break;
                default:
                    throw new RuntimeException("Unknown repository implementation");
            }
        }

        return instance;
    }
}
