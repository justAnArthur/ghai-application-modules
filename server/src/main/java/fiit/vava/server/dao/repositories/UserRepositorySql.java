package fiit.vava.server.dao.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fiit.vava.server.User;
import fiit.vava.server.UserRole;
import fiit.vava.server.infrastructure.DBConnection;

public class UserRepositorySql extends UserRepository {
  @Override
  public List<User> findAll() {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM \"User\"";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql);
           ResultSet rs = pstmt.executeQuery()) {
      while (rs.next()) {
        users.add(mapRowToUser(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  @Override
  public User findById(String id) {
    String sql = "SELECT * FROM \"User\" WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, id);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapRowToUser(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public User save(User toSave) {
    // NOTE: We consider that id can be null, and it must be assigned as null if user is new
    String sql = toSave.getId() == null ?
                 "INSERT INTO \"User\" (email, password, confirmed, role) VALUES (?, ?, ?, ?) RETURNING id" :
                 "UPDATE \"User\" SET email = ?, password = ?, confirmed = ?, role = ? WHERE id = ? RETURNING id";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      pstmt.setString(1, toSave.getEmail());
      pstmt.setString(2, toSave.getPassword());
      pstmt.setBoolean(3, toSave.getConfirmed());
      pstmt.setString(4, toSave.getRole().name());
      if (toSave.getId() != null) {
        pstmt.setString(5, toSave.getId());
      }
      User.Builder userBuilder = User.newBuilder(toSave);
      pstmt.executeUpdate();
      try (ResultSet rs = pstmt.getGeneratedKeys()) {
        if (rs.next()) {
          userBuilder.setId(rs.getString(1));
        }
      }
      return userBuilder.build();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    String sql = "SELECT * FROM \"User\" WHERE email = ?";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, email);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapRowToUser(rs));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public boolean setConfirmed(User requested) {
    String sql = "UPDATE \"User\" SET confirmed = true WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, requested.getId());
      int affectedRows = pstmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private User mapRowToUser(ResultSet rs) throws SQLException {
    User.Builder user = User.newBuilder()
    .setId(rs.getString("id"))
    .setEmail(rs.getString("email"))
    .setPassword(rs.getString("password")) // Consider security implications
    .setConfirmed(rs.getBoolean("confirmed"))
    .setRole(UserRole.valueOf(rs.getString("role"))); // Adjust to match the Role enum in your User cla
    return user.build();
  }
}
