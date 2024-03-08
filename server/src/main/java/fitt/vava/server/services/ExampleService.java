package fitt.vava.server.services;

import fitt.vava.server.ExampleServiceGrpc;
import fitt.vava.server.Request;
import fitt.vava.server.Response;
import io.grpc.stub.StreamObserver;

public class ExampleService extends ExampleServiceGrpc.ExampleServiceImplBase {
    @Override
    public void hello(Request req, StreamObserver<Response> responseObserver) {
        Response reply = Response.newBuilder().setMessage("Hello to " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
