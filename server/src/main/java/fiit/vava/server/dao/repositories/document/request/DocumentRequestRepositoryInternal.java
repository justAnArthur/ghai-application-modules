package fiit.vava.server.dao.repositories.document.request;

import fiit.vava.server.DocumentRequest;
import fiit.vava.server.DocumentRequestStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentRequestRepositoryInternal extends DocumentRequestRepository {

    private final ArrayList<DocumentRequest> documentRequests = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger("server." + DocumentRequestRepositoryInternal.class);

    public DocumentRequest save(DocumentRequest toSave) {
        logger.debug("Saving document request: " + toSave.getId() + " " + toSave.getDocument() + " " + toSave.getStatus());
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

    @Override
    public List<DocumentRequest> findAllByStatus(DocumentRequestStatus status) {
        return documentRequests.stream()
                .filter(documentRequest -> documentRequest.getStatus().equals(status))
                .toList();
    }
}
