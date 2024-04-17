package fiit.vava.server.services;

import fiit.vava.server.*;
import fiit.vava.server.dao.repositories.user.coworker.CoworkerRepository;
import io.grpc.stub.StreamObserver;

import java.util.List;


public class AdminService extends AdminServiceGrpc.AdminServiceImplBase {
    private final CoworkerRepository coworkerRepository;

    public AdminService() {
        this.coworkerRepository = CoworkerRepository.getInstance();
    }

    @Override
    public void registerCoworker(Coworker requestedCoworker, StreamObserver<Response> responseObserver) {
        try {
            Coworker savedCoworker = coworkerRepository.save(requestedCoworker);
            // Build and send a success response
            Response response = Response.newBuilder()
                    .setUser(savedCoworker.getUser())
                    .build();
            responseObserver.onNext(response);
        } catch (Exception ex) {
            Response response = Response.newBuilder()
                    .setError(ex.getMessage())
                    .build();
            responseObserver.onNext(response);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getCoworker(Coworker requestedCoworker, StreamObserver<Response> responseObserver) {
        try {
            Coworker storedCoworker = coworkerRepository.findById(requestedCoworker.getId());

            if (storedCoworker == null)
                throw new IllegalArgumentException("Coworker not found with ID: " + requestedCoworker.getId());

            Response response = Response.newBuilder()
                    .setUser(storedCoworker.getUser()) // Assuming you want to return user details
                    .build();
            responseObserver.onNext(response);
        } catch (Exception ex) {
            Response response = Response.newBuilder()
                    .setError(ex.getMessage())
                    .build();
            responseObserver.onNext(response);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateCoworker(Coworker requestedCoworker, StreamObserver<Response> responseObserver) {
        try {
            // You might want to add logic to check if the coworker exists before updating
            Coworker updatedCoworker = coworkerRepository.save(requestedCoworker);

            Response response = Response.newBuilder()
                    .setUser(updatedCoworker.getUser()) // Assuming you want to return the updated user details
                    .build();
            responseObserver.onNext(response);
        } catch (Exception ex) {
            Response response = Response.newBuilder()
                    .setError(ex.getMessage())
                    .build();
            responseObserver.onNext(response);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteCoworker(Coworker requestedCoworker, StreamObserver<Response> responseObserver) {
        try {
            Coworker coworkerToDelete = coworkerRepository.findById(requestedCoworker.getId());
            if (coworkerToDelete == null)
                throw new IllegalArgumentException("Coworker not found with ID: " + requestedCoworker.getId());

            // Assuming your CoworkerRepository has a delete method or you handle deletion logic
            // coworkerRepository.delete(coworkerToDelete); 

            Response response = Response.newBuilder()
                    .build(); // Empty response to indicate success
            responseObserver.onNext(response);
        } catch (Exception ex) {
            Response response = Response.newBuilder()
                    .setError(ex.getMessage())
                    .build();
            responseObserver.onNext(response);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getAllCoworkers(Empty request, StreamObserver<CoworkerResponse> responseObserver) {
        try {
            List<Coworker> coworkers = coworkerRepository.findAll();
            CoworkerResponse response = CoworkerResponse.newBuilder()
                    .addAllCoworker(coworkers)
                    .build();
            responseObserver.onNext(response);
        } catch (Exception ex) {
            // Handle error (e.g., log the error)
        } finally {
            responseObserver.onCompleted();
        }
    }       
}
