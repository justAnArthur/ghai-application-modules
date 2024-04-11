package fiit.vava.server.dao.repositories.document;

import fiit.vava.server.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentRepositoryInternal extends DocumentRepository {

    private final ArrayList<Document> documents = new ArrayList<>();

    public Document save(Document toSave) {
        if (toSave.getId() != null && !toSave.getId().isEmpty()) {
            Document finalToSave = toSave;
            documents.removeIf(document -> document.getId().equals(finalToSave.getId()));
        } else
            toSave = toSave.toBuilder().setId(UUID.randomUUID().toString()).build();

        documents.add(toSave);
        return toSave;
    }

    @Override
    public Document findById(String id) {
        return documents.stream()
                .filter(document -> document.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Document> findAll() {
        return documents;
    }
}
