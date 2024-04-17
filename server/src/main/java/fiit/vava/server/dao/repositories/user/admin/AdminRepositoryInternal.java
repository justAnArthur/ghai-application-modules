package fiit.vava.server.dao.repositories.user.admin;

import fiit.vava.server.Admin;
import fiit.vava.server.User;
import fiit.vava.server.dao.repositories.user.UserRepository;

import fiit.vava.server.dao.repositories.user.UserRepositoryInternal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdminRepositoryInternal extends AdminRepository {

  private final List<Admin> admins = new ArrayList<>();
  private final static UserRepository userRepo = new UserRepositoryInternal();

  @Override
  public Admin save(Admin toSave) {
    if (toSave.getId() != null && !toSave.getId().isEmpty()) {
      // Update an existing admin
      Admin finalToSave = toSave;
      admins.removeIf(admin -> admin.getId().equals(finalToSave.getId()));
    } else {
      // Assign a new UUID if saving a new admin
      toSave = Admin.newBuilder()
               .setId(UUID.randomUUID().toString())
               .setUser(toSave.getUser())
               .build();
    }

    admins.add(toSave);
    return toSave;
  }

  @Override
  public List<Admin> findAll() {
    // Return a new list to avoid modification of the internal list
    return new ArrayList<>(admins);
  }

  @Override
  public Admin findById(String id) {
    return admins.stream()
           .filter(admin -> admin.getId().equals(id))
           .findFirst()
           .orElse(null);
  }

  @Override
  public Admin findByUserId(String userId) {
    return admins.stream()
           .filter(admin -> admin.getUser().getId().equals(userId))
           .findFirst()
           .orElse(null);
  }

  @Override
  public List<Admin> getNonConfirmedAdmins() {
    // This method needs an additional implementation to check the confirmation status
    // Assuming we have access to a user repository or a method to check user's confirmation status
    // Here we will use a mock approach as an example
    return admins.stream()
    .filter(admin -> {
      User user = userRepo.findById(admin.getUser().getId());
      return user != null && !user.getConfirmed();
    })
    .collect(Collectors.toList());
  }
}
