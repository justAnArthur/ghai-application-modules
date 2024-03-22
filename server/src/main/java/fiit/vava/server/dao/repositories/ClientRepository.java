package fiit.vava.server.dao.repositories;

import fiit.vava.server.Client;
import fiit.vava.server.User;
import fiit.vava.server.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientRepository implements IRepository<Client> {

    private static ClientRepository instance = null;

    private ClientRepository() {
    }

    public static synchronized ClientRepository getInstance() {
        if (instance == null)
            instance = new ClientRepository();

        return instance;
    }

    private final ArrayList<Client> clients = new ArrayList<>() {{
        add(Client.newBuilder().setFirstName("first").setLastName("first").setUser(
                User.newBuilder().setEmail("first@first.first").setPassword("first").setRole(UserRole.CLIENT).setConfirmed(false).build()
        ).build());
    }};

    public Client save(Client toSave) {
        clients.add(toSave);
        return toSave;
    }

    public List<Client> findAll() {
        return clients;
    }

    /*
     * TODO add filtering by coworker
     */
    public List<Client> getNonConfirmedClients() {
        return clients.stream().filter(client -> !client.getUser().getConfirmed()).collect(Collectors.toList());
    }
}
