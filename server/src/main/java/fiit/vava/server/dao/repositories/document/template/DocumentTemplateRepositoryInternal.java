package fiit.vava.server.dao.repositories.document.template;

import fiit.vava.server.DocumentTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentTemplateRepositoryInternal extends DocumentTemplateRepository {

    private final ArrayList<DocumentTemplate> documentTemplates = new ArrayList<>();

    public DocumentTemplate save(DocumentTemplate toSave) {
        if (toSave.getId() != null && !toSave.getId().isEmpty()) {
            DocumentTemplate finalToSave = toSave;
            documentTemplates.removeIf(client -> client.getId().equals(finalToSave.getId()));
        } else
            toSave = toSave.toBuilder().setId(UUID.randomUUID().toString()).build();

        documentTemplates.add(toSave);
        return toSave;
    }

    @Override
    public DocumentTemplate findById(String id) {
        return documentTemplates.stream()
                .filter(documentTemplate -> documentTemplate.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<DocumentTemplate> findAll() {
        return documentTemplates;
    }
}
