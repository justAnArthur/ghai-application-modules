package fiit.vava.server.dao.repositories.user.admin;

import fiit.vava.server.Admin;
import fiit.vava.server.dao.repositories.IRepository;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public abstract class AdminRepository implements IRepository<Admin> {

  private static AdminRepository instance = null;

  public static synchronized AdminRepository getInstance() {
    if (instance == null) {
      Dotenv dotenv = Dotenv.load();

      switch (dotenv.get("REPOSITORY_TYPE")) {
      case "internal":
        // instance = new AdminRepositoryInternal();
        // break;
        throw new RuntimeException("Unknown repository implementation");
      case "sql":
        instance = new AdminRepositorySql();
        break;
      default:
        throw new RuntimeException("Unknown repository implementation");
      }
    }

    return instance;
  }

  /*
   * TODO add filtering by coworker
   */
  public abstract List<Admin> getNonConfirmedAdmins();

  public abstract Admin findByUserId(String id);
}
