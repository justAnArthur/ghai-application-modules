package fiit.vava.server.dao.repositories;

import fiit.vava.server.Client;
import fiit.vava.server.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientRepositoryInternal extends ClientRepository {

    private final ArrayList<Client> clients = new ArrayList<>() {{
        add(Client.newBuilder().setId("1").setFirstName("first").setLastName("first").setUser(
                User.newBuilder().setId("1").build()
        ).build());
    }};

    public Client save(Client toSave) {
        if (toSave.getId() != null)
            clients.removeIf(client -> client.getId().equals(toSave.getId()));

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
    public List<Client> getNonConfirmedClients() {
        return findAll().stream().filter(client -> !client.getUser().getConfirmed()).collect(Collectors.toList());
    }
}
