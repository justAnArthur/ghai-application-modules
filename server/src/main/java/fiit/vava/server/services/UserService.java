package fiit.vava.server.services;

import fiit.vava.server.*;
import fiit.vava.server.config.Constants;
import fiit.vava.server.dao.repositories.document.DocumentRepository;
import fiit.vava.server.dao.repositories.document.request.DocumentRequestRepository;
import fiit.vava.server.dao.repositories.document.template.DocumentTemplateRepository;
import fiit.vava.server.dao.repositories.user.UserRepository;
import fiit.vava.server.dao.repositories.user.client.ClientRepository;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final DocumentRepository documentRepository;
    private final DocumentRequestRepository documentRequestRepository;
    private final DocumentTemplateRepository documentTemplateRepository;

    public UserService() {
        this.documentRepository = DocumentRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
        this.clientRepository = ClientRepository.getInstance();
        this.documentRequestRepository = DocumentRequestRepository.getInstance();
        this.documentTemplateRepository = DocumentTemplateRepository.getInstance();
    }

    public User authorize(String email, String password) throws SecurityException, NoSuchElementException {
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);

        if (!user.getPassword().equals(password))
            throw new SecurityException();

        return user;
    }

    @Override
    public void me(Empty request, StreamObserver<User> responseObserver) {
        User user = Constants.USER_CONTEXT.get();

        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }

    @Override
    @SkipAuthorization
    public void registerClient(RegisterClientRequest request, StreamObserver<Response> responseObserver) {
        try {
            User user = userRepository.save(request.getClient().getUser().toBuilder()
                    .setStatus(UserState.PENDING).build());

            Client client = clientRepository.save(request.getClient().toBuilder()
                    .setUser(user).build());

            // creating document request (will not be used as plain document request) for client passport

            String name = user.getEmail() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            String path = DocumentService.saveFile(name, request.getDocumentFile().toByteArray());

            Document document = documentRepository.save(Document.newBuilder()
                    .setName(name)
                    .setPath(path)
                    .build());

            documentRequestRepository.save(DocumentRequest.newBuilder()
                    .setClient(client)
                    .setTemplate(documentTemplateRepository.getClientPassportTemplate())
                    .setStatus(DocumentRequestStatus.CREATED)
                    .setDocument(document)
                    .build());

            Response response = Response.newBuilder()
                    .setUser(user).build();

            responseObserver.onNext(response);
        } catch (Exception ex) {
            Response response = Response.newBuilder()
                    .setError(ex.getMessage()).build();

            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }
}
