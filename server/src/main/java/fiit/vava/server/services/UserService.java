package fiit.vava.server.services;

import fiit.vava.server.*;
import fiit.vava.server.dao.repositories.UserRepository;
import io.grpc.stub.StreamObserver;

import java.util.NoSuchElementException;

public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = UserRepository.getInstance();
    }

    public User authorize(String email, String password) throws SecurityException, NoSuchElementException {
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);

        if (!user.getPassword().equals(password))
            throw new SecurityException();

        return user;
    }

    @Override
    public void me(Empty request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(Constants.USER_CONTEXT.get());
        responseObserver.onCompleted();
    }

    @Override
    @SkipAuthorization
    public void registerClient(Client request, StreamObserver<Response> responseObserver) {
        super.registerClient(request, responseObserver);
    }
}
