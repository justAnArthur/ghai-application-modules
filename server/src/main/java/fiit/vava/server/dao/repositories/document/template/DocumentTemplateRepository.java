package fiit.vava.server.dao.repositories.document.template;

import fiit.vava.server.DocumentTemplate;
import fiit.vava.server.dao.repositories.IRepository;
import io.github.cdimascio.dotenv.Dotenv;

public abstract class DocumentTemplateRepository implements IRepository<DocumentTemplate> {

    private static DocumentTemplateRepository instance = null;

    public static synchronized DocumentTemplateRepository getInstance() {
        if (instance == null) {
            Dotenv dotenv = Dotenv.load();

            switch (dotenv.get("REPOSITORY_TYPE")) {
                case "internal":
                    instance = new DocumentTemplateRepositoryInternal();
                    break;
                case "sql":
                    // instance = new ClientRepositorySql();
                    break;
                default:
                    throw new RuntimeException("Unknown repository implementation");
            }
        }

        return instance;
    }
}
