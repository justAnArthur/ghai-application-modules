package fiit.vava.server.dao.repositories.user.client;

import fiit.vava.server.Client;
import fiit.vava.server.User;
import fiit.vava.server.dao.repositories.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientRepositoryInternal extends ClientRepository {

    private final ArrayList<Client> clients = new ArrayList<>() {{
        add(Client.newBuilder().setId("1").setFirstName("first").setLastName("first").setUser(User.newBuilder()
                .setId("1").build()).build());
        add(Client.newBuilder().setId("2").setFirstName("client").setLastName("client").setUser(User.newBuilder()
                .setId("5").build()).build());
        add(Client.newBuilder().setId("3").setFirstName("coworker").setLastName("coworker").setUser(User.newBuilder()
                .setId("6").build()).build());
    }};

    public Client save(Client toSave) {
        if (toSave.getId() != null && !toSave.getId().isEmpty()) {
            Client finalToSave = toSave;
            clients.removeIf(client -> client.getId().equals(finalToSave.getId()));
        } else
            toSave = toSave.toBuilder().setId(UUID.randomUUID().toString()).build();

        clients.add(toSave);
        return toSave;
    }

    @Override
    public Client findById(String id) {
        return clients.stream()
                .filter(client -> client.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Client> findAll() {
        UserRepository userRepository = UserRepository.getInstance();

        return clients.stream()
                .map(client -> client.toBuilder()
                        .setUser(userRepository.findAll().stream()
                                .filter(user -> user.getId().equals(client.getUser().getId()))
                                .findFirst()
                                .orElse(null))
                        .build())
                .collect(Collectors.toList());
    }

    /*
     * TODO add filtering by coworker
     */
    @Override
    public List<Client> getNonConfirmedClients() {
        return findAll().stream().filter(client -> !client.getUser().getConfirmed()).collect(Collectors.toList());
    }

    @Override
    public Client findByUserId(String id) {
        return clients.stream()
                .filter(client -> client.getUser().getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
