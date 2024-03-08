package fitt.vava.server;

import fitt.vava.server.services.ExampleService;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Server {

    private final String name = "ServeR";
    public static final int PORT = 50031;

    private io.grpc.Server server;

    private void start() throws IOException {
        server = Grpc.newServerBuilderForPort(PORT, InsecureServerCredentials.create())
                .addService(new ExampleService())
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    Server.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
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
        server.blockUntilShutdown();
    }
}