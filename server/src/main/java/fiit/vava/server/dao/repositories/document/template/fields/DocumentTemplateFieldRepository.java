package fiit.vava.server.dao.repositories.document.template.fields;

import fiit.vava.server.DocumentTemplate;
import fiit.vava.server.DocumentTemplateField;
import fiit.vava.server.dao.repositories.IRepository;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public abstract class DocumentTemplateFieldRepository implements IRepository<DocumentTemplateField> {

    private static DocumentTemplateFieldRepository instance = null;

    public static synchronized DocumentTemplateFieldRepository getInstance() {
        if (instance == null) {
            Dotenv dotenv = Dotenv.load();

            switch (dotenv.get("REPOSITORY_TYPE")) {
                case "internal":
                    instance = new DocumentTemplateFieldRepositoryInternal();
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

    public abstract List<DocumentTemplateField> findAllByDocumentTemplate(DocumentTemplate documentTemplate);
}
