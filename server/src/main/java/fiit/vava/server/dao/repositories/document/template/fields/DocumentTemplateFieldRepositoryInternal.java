package fiit.vava.server.dao.repositories.document.template.fields;

import fiit.vava.server.DocumentTemplate;
import fiit.vava.server.DocumentTemplateField;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentTemplateFieldRepositoryInternal extends DocumentTemplateFieldRepository {

    private final ArrayList<DocumentTemplateField> documentTemplateFields = new ArrayList<>();

    public DocumentTemplateField save(DocumentTemplateField toSave) {
        if (toSave.getId() != null && !toSave.getId().isEmpty()) {
            DocumentTemplateField finalToSave = toSave;
            documentTemplateFields.removeIf(documentTemplateField -> documentTemplateField.getId().equals(finalToSave.getId()));
        } else
            toSave = toSave.toBuilder().setId(UUID.randomUUID().toString()).build();

        documentTemplateFields.add(toSave);
        return toSave;
    }

    @Override
    public DocumentTemplateField findById(String id) {
        return documentTemplateFields.stream()
                .filter(documentTemplateField -> documentTemplateField.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<DocumentTemplateField> findAll() {
        return documentTemplateFields;
    }

    @Override
    public List<DocumentTemplateField> findAllByDocumentTemplate(DocumentTemplate documentTemplate) {
        return documentTemplateFields.stream()
                .filter(documentTemplateField -> documentTemplateField.getTemplate().getId().equals(documentTemplate.getId()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
