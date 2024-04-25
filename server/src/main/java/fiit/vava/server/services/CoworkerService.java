package fiit.vava.server.services;

import fiit.vava.server.*;
import fiit.vava.server.dao.repositories.user.UserRepository;
import fiit.vava.server.dao.repositories.user.client.ClientRepository;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class CoworkerService extends CoworkerServiceGrpc.CoworkerServiceImplBase {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public CoworkerService() {
        this.userRepository = UserRepository.getInstance();
        this.clientRepository = ClientRepository.getInstance();
    }

    @Override
    public void getNonApprovedClients(Empty request, StreamObserver<NonApprovedClientsResponse> responseObserver) {
        List<Client> clients = clientRepository.getNonConfirmedClients();

        NonApprovedClientsResponse.Builder builder = NonApprovedClientsResponse.newBuilder();

        clients.forEach(builder::addClient);

        NonApprovedClientsResponse response = builder.build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void approveClient(Client request, StreamObserver<Response> responseObserver) {
        boolean result = userRepository.setConfirmed(request.getUser());

        Response.Builder builder = Response.newBuilder();

        if (!result)
            builder.setError("unable to approve");

        Response response = builder.build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
