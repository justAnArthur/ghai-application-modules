package fiit.vava.server.application.impl.repo;

import fiit.vava.server.Client;
import fiit.vava.server.Coworker;
import fiit.vava.server.User;
import fiit.vava.server.domain.repo.IUserRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepoMock implements IUserRepo {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Client> clients = new HashMap<>();
    private final Map<String, Coworker> coworkers = new HashMap<>();

    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    @Override
    public Client registerClient(Client client) {
        clients.put(client.getId(), client);
        return client;
    }

    @Override
    public boolean changeClientStatus(String clientId, boolean confirmed) {
        if (clients.containsKey(clientId)) {
            Client client = clients.get(clientId);
            return true;
        }
        return false;
    }

    @Override
    public Coworker saveCoworker(Coworker coworker) {
        coworkers.put(coworker.getId(), coworker);
        return coworker;
    }

    @Override
    public Optional<Client> findClientById(String clientId) {
        return Optional.ofNullable(clients.get(clientId));
    }

    @Override
    public Optional<Coworker> findCoworkerById(String coworkerId) {
        return Optional.ofNullable(coworkers.get(coworkerId));
    }
}
