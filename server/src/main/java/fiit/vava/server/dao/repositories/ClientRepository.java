package fiit.vava.server.dao.repositories;

import fiit.vava.server.Client;

import java.util.ArrayList;

public class ClientRepository implements IRepository<Client> {

    private static ClientRepository instance = null;

    private ClientRepository() {
    }

    public static synchronized ClientRepository getInstance() {
        if (instance == null)
            instance = new ClientRepository();

        return instance;
    }


    private final ArrayList<Client> clients = new ArrayList<>();

    public Client save(Client toSave) {
        clients.add(toSave);
        return toSave;
    }

    public ArrayList<Client> findAll() {
        return clients;
    }
}
