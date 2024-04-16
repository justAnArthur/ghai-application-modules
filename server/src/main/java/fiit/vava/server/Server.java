package fiit.vava.server;

import fiit.vava.server.infrastructure.DBseed;
import fiit.vava.server.services.DocumentService;
import fiit.vava.server.services.UserService;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class.toString());

    public static final int PORT = 50031;

    private io.grpc.Server server;

    private void start() throws IOException {
        // DBseed.seed();
        UserService userService = new UserService();
        DocumentService documentService = new DocumentService();

        AuthorizationServerInterceptor interceptor = new AuthorizationServerInterceptor(userService);

        server = Grpc.newServerBuilderForPort(PORT, InsecureServerCredentials.create())
                .addService(userService)
                .addService(documentService)
                .intercept(interceptor)
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                Server.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));

        logger.info("Server started, listening on " + PORT);
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final Server server = new Server();
        server.start();
        logger.info("Server started");

        server.blockUntilShutdown();
    }
}
