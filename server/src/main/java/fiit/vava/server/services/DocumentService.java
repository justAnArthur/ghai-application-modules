package fiit.vava.server.services;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import fiit.vava.server.*;
import fiit.vava.server.config.Constants;
import fiit.vava.server.dao.repositories.document.DocumentRepository;
import fiit.vava.server.dao.repositories.document.field.DocumentFieldRepository;
import fiit.vava.server.dao.repositories.document.request.DocumentRequestRepository;
import fiit.vava.server.dao.repositories.document.template.DocumentTemplateRepository;
import fiit.vava.server.dao.repositories.document.template.fields.DocumentTemplateFieldRepository;
import fiit.vava.server.dao.repositories.user.client.ClientRepository;
import io.github.cdimascio.dotenv.Dotenv;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DocumentService extends DocumentServiceGrpc.DocumentServiceImplBase {

    DocumentRepository documentRepository;
    DocumentTemplateRepository documentTemplateRepository;
    DocumentTemplateFieldRepository documentTemplateFieldRepository;
    DocumentRequestRepository documentRequestRepository;
    DocumentFieldRepository documentFieldRepository;
    ClientRepository clientRepository;

    private final String PATH_TO_SAVE_FILES;

    public DocumentService() {
        this.documentRepository = DocumentRepository.getInstance();
        this.documentTemplateRepository = DocumentTemplateRepository.getInstance();
        this.documentTemplateFieldRepository = DocumentTemplateFieldRepository.getInstance();
        this.documentRequestRepository = DocumentRequestRepository.getInstance();
        this.documentFieldRepository = DocumentFieldRepository.getInstance();
        this.clientRepository = ClientRepository.getInstance();

        Dotenv dotenv = Dotenv.load();
        this.PATH_TO_SAVE_FILES = dotenv.get("PATH_TO_SAVE_FILES");
    }

    private String saveFile(String fileName, byte[] data) throws IOException {
        Path path = Paths.get(PATH_TO_SAVE_FILES, fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, data, StandardOpenOption.CREATE);
        return path.toString();
    }

    private ByteString getFileByPath(String path) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(path));
        return ByteString.copyFrom(data);
    }

    @Override
    public void getFileByPath(GetFileByPathRequest request, StreamObserver<GetFileByPathResponse> responseObserver) {
        try {
            responseObserver.onNext(
                    GetFileByPathResponse.newBuilder()
                            .setFile(getFileByPath(request.getPath()))
                            .build()
            );
            responseObserver.onCompleted();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read a file from local file system.");
        }
    }

    @Override
    public void createDocumentTemplate(CreateDocumentTemplateRequest request, StreamObserver<DocumentTemplate> responseObserver) {
        User user = Constants.USER_CONTEXT.get();

        String path;
        try {
            path = saveFile(
                    user.getEmail() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")),
                    request.getFile().toByteArray()
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to save a file to local file system.");
        }

        DocumentTemplate documentTemplate = documentTemplateRepository.save(DocumentTemplate.newBuilder()
                .setName(request.getName())
                .setType(request.getType())
                .setPath(path)
                .build());

        System.out.println("Document template created: " + documentTemplate.getId());

        request.getFieldsList().forEach(field ->
                documentTemplateFieldRepository.save(DocumentTemplateField.newBuilder()
                        .setTemplate(documentTemplate)
                        .setName(field.getName())
                        .setType(field.getType())
                        .setRequired(field.getRequired())
                        .build()));

        responseObserver.onNext(documentTemplate);
        responseObserver.onCompleted();
    }

    @Override
    public void getDocumentTemplateById(GetDocumentTemplateByIdRequest request, StreamObserver<DocumentTemplateWithFields> responseObserver) {
        DocumentTemplate documentTemplate = documentTemplateRepository.findById(request.getId());

        if (documentTemplate == null)
            throw new RuntimeException("Document template not found.");

        DocumentTemplateWithFields response = DocumentTemplateWithFields.newBuilder()
                .setTemplate(documentTemplate)
                .addAllFields(documentTemplateFieldRepository.findAllByDocumentTemplate(documentTemplate))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllDocumentTemplates(Empty request, StreamObserver<GetAllDocumentTemplates> responseObserver) {
        responseObserver.onNext(GetAllDocumentTemplates.newBuilder()
                .addAllTemplates(documentTemplateRepository.findAll())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void createDocumentRequest(CreateDocumentRequest request, StreamObserver<DocumentRequest> responseObserver) {
        User user = Constants.USER_CONTEXT.get();

        Client client = clientRepository.findByUserId(user.getId());

        DocumentTemplate template = documentTemplateRepository.findById(request.getTemplateId());

        if (template == null)
            throw new RuntimeException("document template not found.");

        DocumentRequestStatus status = DocumentRequestStatus.CREATED;

        DocumentRequest documentRequest = documentRequestRepository.save(
                DocumentRequest.newBuilder()
                        .setClient(client)
                        .setTemplate(template)
                        .setStatus(status)
                        .setCreatedAt(Timestamp.newBuilder().setNanos(Instant.now().getNano()).build())
                        .build());

        request.getFieldsList().forEach(field ->
                documentFieldRepository.save(field.toBuilder()
                        .setRequest(documentRequest)
                        .build()
                ));

        responseObserver.onNext(documentRequest);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllMineDocumentRequests(Empty request, StreamObserver<GetAllDocumentRequestsResponse> responseObserver) {
        User user = Constants.USER_CONTEXT.get();

        Client client = clientRepository.findByUserId(user.getId());

        List<DocumentRequest> documentRequests = documentRequestRepository.findAllByClientId(client.getId());

        GetAllDocumentRequestsResponse response = GetAllDocumentRequestsResponse.newBuilder()
                .addAllDocumentRequests(documentRequests)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getDocumentRequestById(GetDocumentRequestByIdRequest request, StreamObserver<DocumentRequest> responseObserver) {
        DocumentRequest documentRequest = documentRequestRepository.findById(request.getId());

        if (documentRequest == null)
            throw new RuntimeException("Document request not found.");

        responseObserver.onNext(documentRequest);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllDocumentRequestsToApprove(Empty request, StreamObserver<GetAllDocumentRequestsResponse> responseObserver) {
        // TODO: filter by coworker's admin region
        List<DocumentRequest> documentRequests = documentRequestRepository.findAllByStatus(DocumentRequestStatus.CREATED);

        GetAllDocumentRequestsResponse response = GetAllDocumentRequestsResponse.newBuilder()
                .addAllDocumentRequests(documentRequests)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllDocumentFieldsByDocumentRequestId(GetDocumentFieldsByDocumentRequestIdRequest request, StreamObserver<DocumentFieldsResponse> responseObserver) {
        List<DocumentField> fields = documentFieldRepository.findAllByDocumentRequestId(request.getDocumentRequestId());

        DocumentFieldsResponse response = DocumentFieldsResponse.newBuilder()
                .addAllFields(fields)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Document generateDocumentBasedOnRequest(DocumentRequest documentRequest) throws IOException {
        // now - just save a copy of file
        String name = documentRequest.getClient().getUser().getEmail() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        String path = saveFile(name, getFileByPath(documentRequest.getTemplate().getPath()).toByteArray());

        return Document.newBuilder()
                .setName(name)
                .setPath(path)
                .build();
    }

    @Override
    public void approveDocumentRequest(ApproveRejectDocumentRequestRequest request, StreamObserver<Response> responseObserver) {
        DocumentRequest documentRequest = documentRequestRepository.findById(request.getDocumentRequestId());

        if (documentRequest == null)
            throw new RuntimeException("Document request not found.");

        Document document = null;
        try {
            document = generateDocumentBasedOnRequest(documentRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        documentRequestRepository.save(documentRequest.toBuilder()
                .setDocument(document)
                .setStatus(DocumentRequestStatus.VALIDATED)
                .build());

        // todo notify client

        responseObserver.onNext(Response.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void rejectDocumentRequest(ApproveRejectDocumentRequestRequest request, StreamObserver<Response> responseObserver) {
        DocumentRequest documentRequest = documentRequestRepository.findById(request.getDocumentRequestId());

        if (documentRequest == null)
            throw new RuntimeException("Document request not found.");

        documentRequestRepository.save(documentRequest.toBuilder()
                .setStatus(DocumentRequestStatus.DISCARDED)
                .build());

        // todo notify client

        responseObserver.onNext(Response.newBuilder().build());
        responseObserver.onCompleted();
    }
}
