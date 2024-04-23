package fiit.vava.server.dao.repositories.document.request;

import fiit.vava.server.DocumentRequest;
import fiit.vava.server.DocumentRequestStatus;
import fiit.vava.server.dao.repositories.IRepository;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public abstract class DocumentRequestRepository implements IRepository<DocumentRequest> {

    private static DocumentRequestRepository instance = null;

    public static synchronized DocumentRequestRepository getInstance() {
        if (instance == null) {
            Dotenv dotenv = Dotenv.load();

            switch (dotenv.get("REPOSITORY_TYPE")) {
                case "internal":
                    instance = new DocumentRequestRepositoryInternal();
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

    public abstract List<DocumentRequest> findAllByClientId(String clientId);

    public abstract List<DocumentRequest> findAllByStatus(DocumentRequestStatus status);
}
