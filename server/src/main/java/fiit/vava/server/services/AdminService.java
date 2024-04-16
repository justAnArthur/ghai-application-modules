package fiit.vava.server.services;

import java.util.List;

import fiit.vava.server.*;
import fiit.vava.server.dao.repositories.user.coworker.CoworkerRepository;
import io.grpc.stub.StreamObserver;


public class AdminService extends AdminServiceGrpc.AdminServiceImplBase {
    private final CoworkerRepository coworkerRepository;

    public AdminService() {
        this.coworkerRepository = CoworkerRepository.getInstance();
    }

    @Override
    public void registerCoworker(CoworkerRequest request, StreamObserver<Response> responseObserver) {
        try {
            Coworker coworker = request.getCoworker();
            // TODO: Add validation of coworker data (optional)
            Coworker savedCoworker = coworkerRepository.save(coworker);
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
    public void getCoworker(CoworkerRequest request, StreamObserver<Response> responseObserver) {
        try {
            String coworkerId = request.getCoworker().getId();
            Coworker coworker = coworkerRepository.findById(coworkerId);
            if (coworker == null) {
                throw new IllegalArgumentException("Coworker not found with ID: " + coworkerId);
            }

            Response response = Response.newBuilder()
                    .setUser(coworker.getUser()) // Assuming you want to return user details
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
    public void updateCoworker(CoworkerRequest request, StreamObserver<Response> responseObserver) {
        try {
            Coworker coworkerToUpdate = request.getCoworker();
            // You might want to add logic to check if the coworker exists before updating
            Coworker updatedCoworker = coworkerRepository.save(coworkerToUpdate);

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
    public void deleteCoworker(CoworkerRequest request, StreamObserver<Response> responseObserver) {
        try {
            String coworkerId = request.getCoworker().getId();
            Coworker coworkerToDelete = coworkerRepository.findById(coworkerId);
            if (coworkerToDelete == null) {
                throw new IllegalArgumentException("Coworker not found with ID: " + coworkerId);
            }
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
