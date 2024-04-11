package fiit.vava.server.dao.repositories.document.field;

import fiit.vava.server.DocumentField;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentFieldRepositoryInternal extends DocumentFieldRepository {

    private final ArrayList<DocumentField> documentFields = new ArrayList<>();

    public DocumentField save(DocumentField toSave) {
        if (toSave.getId() != null && !toSave.getId().isEmpty()) {
            DocumentField finalToSave = toSave;
            documentFields.removeIf(documentField -> documentField.getId().equals(finalToSave.getId()));
        } else
            toSave = toSave.toBuilder().setId(UUID.randomUUID().toString()).build();

        documentFields.add(toSave);
        return toSave;
    }

    @Override
    public DocumentField findById(String id) {
        return documentFields.stream()
                .filter(documentField -> documentField.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<DocumentField> findAll() {
        return documentFields;
    }
}
