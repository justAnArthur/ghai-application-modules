package fiit.vava.server.domain.repo;

import java.util.Optional;

import fiit.vava.server.Client;
import fiit.vava.server.Coworker;
import fiit.vava.server.User;

public interface IUserRepo {
  // TODO: Add save method
  User create(User user);

  Optional<User> findByEmail(String email);

  Client registerClient(Client client);

  boolean changeClientStatus(String clientId, boolean confirmed);

  Coworker saveCoworker(Coworker coworker);

  Optional<Client> findClientById(String clientId);

  Optional<Coworker> findCoworkerById(String coworkerId);
}
