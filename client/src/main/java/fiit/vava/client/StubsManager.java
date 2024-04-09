package fiit.vava.client;

import fiit.vava.client.callers.BearerToken;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.Server;
import fiit.vava.server.UserServiceGrpc;
import fiit.vava.server.config.Constants;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class StubsManager {
    private static final Map<Class<?>, Method> newStubMethods = new HashMap<>();

    private static final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", Server.PORT)
            .usePlaintext()
            .build();

    private static StubsManager instance;

    public static StubsManager getInstance() {
        if (instance == null) {
            instance = new StubsManager();
        }
        return instance;
    }

    private StubsManager() {
    }

    public <T extends io.grpc.stub.AbstractBlockingStub<?>> T appendCredentials(T stub) {
        String[] credentials = CredentialsManager.retrieveCredentials();

        if (credentials != null) {
            BearerToken token = new BearerToken(
                    Jwts.builder()
                            .setSubject(credentials[0])
                            .claim("password", credentials[1])
                            .signWith(SignatureAlgorithm.HS256, Constants.JWT_SIGNING_KEY)
                            .compact()
            );

            stub = (T) stub.withCallCredentials(token);
        }

        return stub;
    }

    public UserServiceGrpc.UserServiceBlockingStub getUserServiceBlockingStub() {
        return (UserServiceGrpc.UserServiceBlockingStub) appendCredentials(UserServiceGrpc.newBlockingStub(channel));
    }

    public DocumentServiceGrpc.DocumentServiceBlockingStub getDocumentServiceBlockingStub() {
        return (DocumentServiceGrpc.DocumentServiceBlockingStub) appendCredentials(DocumentServiceGrpc.newBlockingStub(channel));
    }
}
