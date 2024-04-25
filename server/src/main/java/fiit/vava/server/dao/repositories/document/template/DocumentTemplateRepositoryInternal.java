package fiit.vava.server.dao.repositories.document.template;

import fiit.vava.server.DocumentTemplate;
import fiit.vava.server.DocumentTemplateType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentTemplateRepositoryInternal extends DocumentTemplateRepository {

    private static String CLIENT_PASSPORT_NAME = "Client Passport";

    private final ArrayList<DocumentTemplate> documentTemplates = new ArrayList<>() {{
        add(DocumentTemplate.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setName(CLIENT_PASSPORT_NAME)
                .setType(DocumentTemplateType.LEGAL)
                .setPrivate(true)
                .build());
    }};

    public DocumentTemplate save(DocumentTemplate toSave) {
        if (toSave.getId() != null && !toSave.getId().isEmpty()) {
            DocumentTemplate finalToSave = toSave;
            documentTemplates.removeIf(documentTemplate -> documentTemplate.getId().equals(finalToSave.getId()));
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

    @Override
    public List<DocumentTemplate> findAll() {
        return documentTemplates;
    }

    @Override
    public DocumentTemplate getClientPassportTemplate() {
        return documentTemplates.stream()
                .filter(documentTemplate -> documentTemplate.getName().equals(CLIENT_PASSPORT_NAME))
                .findFirst()
                .orElse(null);
    }
}
