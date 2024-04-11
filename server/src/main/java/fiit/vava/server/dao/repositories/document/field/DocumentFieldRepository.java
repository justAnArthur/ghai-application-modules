package fiit.vava.server.dao.repositories.document.field;

import fiit.vava.server.DocumentField;
import fiit.vava.server.dao.repositories.IRepository;
import io.github.cdimascio.dotenv.Dotenv;

public abstract class DocumentFieldRepository implements IRepository<DocumentField> {

    private static DocumentFieldRepository instance = null;

    public static synchronized DocumentFieldRepository getInstance() {
        if (instance == null) {
            Dotenv dotenv = Dotenv.load();

            switch (dotenv.get("REPOSITORY_TYPE")) {
                case "internal":
                    instance = new DocumentFieldRepositoryInternal();
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
