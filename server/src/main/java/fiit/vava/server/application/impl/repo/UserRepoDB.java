package fiit.vava.server.application.impl.repo;

import fiit.vava.server.Client;
import fiit.vava.server.Coworker;
import fiit.vava.server.User;
import fiit.vava.server.domain.repo.IUserRepo;
import fiit.vava.server.infrastructure.DbConnection;

import java.sql.*;
import java.util.Optional;

public class UserRepoDB implements IUserRepo {

    public User create(User user) {
        String sql = "INSERT INTO \"User\" (id, email, password, confirmed, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword()); // Ensure this password is hashed
            pstmt.setBoolean(4, user.getConfirmed());
            pstmt.setString(5, user.getRole().name());
            pstmt.executeUpdate();
            return user; // Assuming a constructor or builder for User that fits this context
        } catch (SQLException e) {
            // Log or handle the error appropriately
            e.printStackTrace();
        }
        return null;
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM \"User\" WHERE email = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = User.newBuilder() 
                        .setId(rs.getString("id"))
                        .setEmail(rs.getString("email"))
                        .build();
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Client registerClient(Client client) {
    // Create user in the first place
        String sql = "INSERT INTO \"Client\" (id, user_id, registration_date, first_name, last_name, date_of_birth, region_in_new_country, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getId());
            pstmt.setString(2, client.getUser().getId()); // Adjust based on how the User is linked
            // Add other parameters as needed, converting types appropriately
            pstmt.executeUpdate();
            return client; // Adjust this according to how your Client objects are constructed
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean changeClientStatus(String clientId, boolean confirmed) {
        String sql = "UPDATE \"User\" SET confirmed = ? WHERE id = (SELECT user_id FROM \"Client\" WHERE id = ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, confirmed);
            pstmt.setString(2, clientId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Coworker saveCoworker(Coworker coworker) {
        // Assuming "Coworker" extends "User" with additional fields
        String sql = "INSERT INTO \"Coworker\" (id, name, user_id, created_by, prefered_countries) VALUES (?, ?, ?, ?, ?::text[])";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, coworker.getId());
            pstmt.setString(2, coworker.getName());
            pstmt.setString(3, coworker.getUser().getId()); // Adjust based on your object structure
            // You will need to convert preferredCountries from List<String> to Array or similar for PostgreSQL
            // This could require a custom conversion method or using a library like PgJDBC's PGobject for the array type
            pstmt.executeUpdate();
            return coworker; // Adjust this according to how your Coworker objects are constructed
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

  public Optional<Coworker> findCoworkerById(String coworkerId) {
    String sql = "SELECT * FROM \"Coworker\" WHERE id = ?";
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, coworkerId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        Coworker coworker = new Coworker.Builder() // Adjust according to the actual builder or constructor
          .setId(rs.getString("id"))
          .setName(rs.getString("name"))
          // Add other fields as needed
          .build();
        return Optional.of(coworker);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Optional<Client> findClientById(String clientId) {
    String sql = "SELECT * FROM \"Client\" WHERE id = ?";
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, clientId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        Client client = Client.newBuilder() // Adjust according to the actual builder or constructor
          .setId(rs.getString("id"))
          .setRegistrationDate(rs.getDate("registration_date"))
          // Add other fields as needed
          .build();
        return Optional.of(client);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
