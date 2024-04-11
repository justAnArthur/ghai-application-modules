package fiit.vava.server.dao.repositories.document.request;

import fiit.vava.server.DocumentRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentRequestRepositoryInternal extends DocumentRequestRepository {

    private final ArrayList<DocumentRequest> documentRequests = new ArrayList<>();

    public DocumentRequest save(DocumentRequest toSave) {
        if (toSave.getId() != null && !toSave.getId().isEmpty()) {
            DocumentRequest finalToSave = toSave;
            documentRequests.removeIf(documentRequest -> documentRequest.getId().equals(finalToSave.getId()));
        } else
            toSave = toSave.toBuilder().setId(UUID.randomUUID().toString()).build();

        documentRequests.add(toSave);
        return toSave;
    }

    @Override
    public DocumentRequest findById(String id) {
        return documentRequests.stream()
                .filter(documentRequest -> documentRequest.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<DocumentRequest> findAll() {
        return documentRequests;
    }

    @Override
    public List<DocumentRequest> findAllByClientId(String clientId) {
        return documentRequests.stream()
                .filter(documentRequest -> documentRequest.getClient().getId().equals(clientId))
                .toList();
    }
}
