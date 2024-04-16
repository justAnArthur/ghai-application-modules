package fiit.vava.server.infrastructure;

import fiit.vava.server.dao.repositories.document.request.DocumentRequestRepository;
import fiit.vava.server.dao.repositories.user.UserRepository;
import fiit.vava.server.dao.repositories.user.admin.AdminRepository;
import fiit.vava.server.dao.repositories.user.client.ClientRepository;
import fiit.vava.server.dao.repositories.user.coworker.CoworkerRepository;
import fiit.vava.server.User;
import fiit.vava.server.UserRole;
import fiit.vava.server.Coworker;
import fiit.vava.server.Client;
import fiit.vava.server.Admin;
import fiit.vava.server.DocumentRequest;
import fiit.vava.server.DocumentRequestStatus;

import java.util.UUID;

public class DBseed {

  public static void seed() {
    UserRepository userRepository = UserRepository.getInstance();
    AdminRepository adminRepository = AdminRepository.getInstance();
    CoworkerRepository coworkerRepository = CoworkerRepository.getInstance();
    ClientRepository clientRepository = ClientRepository.getInstance();
    DocumentRequestRepository documentRequestRepository = DocumentRequestRepository.getInstance();

    // Create Admins
    for (int i = 1; i <= 3; i++) {
      String name = "admin" + i;
      User adminUser = createUser(name, name, true, UserRole.ADMIN);
      userRepository.save(adminUser);
      Admin admin = Admin.newBuilder()
      .setId(adminUser.getId())
      .build();
      adminRepository.save(admin);
    }

    // Create Coworkers
    for (int i = 1; i <= 3; i++) {
      String name = "coworker" + i;
      User coworkerUser = createUser(name, name, true, UserRole.COWORKER);
      userRepository.save(coworkerUser);
      Coworker coworker = Coworker.newBuilder()
      .setId(coworkerUser.getId())
      .setCreatedBy(adminRepository.findAll().get(0).getId())
      .setPreferredCountries(1, "SK")
      .build();
      coworkerRepository.save(coworker);
    }

    // Create Clients
    String client1Name  = "client1";
    User clientUser1 = createUser(client1Name, client1Name, true, UserRole.CLIENT);
    userRepository.save(clientUser1);
    Client client1 = Client.newBuilder()
    .setId(clientUser1.getId())
    .setRegistrationDate("2020-01-01")
    .setFirstName(client1Name)
    .setLastName(client1Name)
    .setDateOfBirth("1990-01-01")
    .setRegion("Bratislava")
    .setCountry("Slovakia")
    .build();
    clientRepository.save(client1);

    String client2Name = "client2";
    User clientUser2 = createUser(client2Name, client2Name, false, UserRole.CLIENT);
    userRepository.save(clientUser2);
    Client client2 = Client.newBuilder()
    .setId(clientUser2.getId())
    .setRegistrationDate("2020-01-01")
    .setFirstName(client2Name)
    .setLastName(client2Name)
    .setDateOfBirth("1990-01-01")
    .setRegion("Trnava")
    .setCountry("Slovakia")
    .build();
    clientRepository.save(client2);

    // Create a Document Request for client2
    DocumentRequest docReq = DocumentRequest.newBuilder()
    .setClient(client2)
    .setStatus(DocumentRequestStatus.CREATED)
    .build();
    documentRequestRepository.save(docReq);

    String client3Name = "client3";
    User clientUser3 = createUser(client3Name, client3Name, true, UserRole.CLIENT);
    userRepository.save(clientUser3);
    Client client3 = Client.newBuilder()
    .setId(clientUser3.getId())
    .setRegistrationDate("2020-01-01")
    .setFirstName(client3Name)
    .setLastName(client3Name)
    .setDateOfBirth("1990-01-01")
    .setRegion("Nitra")
    .setCountry("Slovakia")
    .build();
    clientRepository.save(client3);
  }

  private static User createUser(String name, String password, boolean confirmed, UserRole role) {
    User user = User.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setEmail(name + "@example.com")
                .setPassword(password)
                .setConfirmed(confirmed)
                .setRole(role).build();
    return user;
  }
}
