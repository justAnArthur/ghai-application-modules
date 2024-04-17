package fiit.vava.server.dao.repositories.user.admin;

import fiit.vava.server.Admin;
import fiit.vava.server.infrastructure.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminRepositorySql extends AdminRepository {

  @Override
  public Admin save(Admin toSave) {
    String sql = toSave.getId() == null ?
                 "INSERT INTO \"Admin\" (user_id) VALUES (?) RETURNING id" :
                 "UPDATE \"Admin\" SET user_id = ? WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
      pstmt.setObject(1, UUID.fromString(toSave.getUser().getId()));
      if (toSave.getId() != null) {
        pstmt.setObject(2, UUID.fromString(toSave.getId()));
      }
      int affectedRows = pstmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Creating/updating admin failed, no rows affected.");
      }
      Admin.Builder adminBuilder = Admin.newBuilder(toSave);
      if (toSave.getId() == null) {
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            adminBuilder.setId(generatedKeys.getString(1));
          } else {
            throw new SQLException("Creating admin failed, no ID obtained.");
          }
        }
      }
      return adminBuilder.build();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Admin findById(String id) {
    String sql = "SELECT * FROM \"Admin\" WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setObject(1, UUID.fromString(id));
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapRowToAdmin(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Admin> findAll() {
    List<Admin> admins = new ArrayList<>();
    String sql = "SELECT * FROM \"Admin\"";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql);
           ResultSet rs = pstmt.executeQuery()) {
      while (rs.next()) {
        admins.add(mapRowToAdmin(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return admins;
  }

  @Override
  public Admin findByUserId(String userId) {
    String sql = "SELECT * FROM \"Admin\" WHERE user_id = ?";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setObject(1, UUID.fromString(userId));
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapRowToAdmin(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Admin> getNonConfirmedAdmins() {
    // Implementation would depend on additional criteria or table structures
    return new ArrayList<>();
  }

  private Admin mapRowToAdmin(ResultSet rs) throws SQLException {
    // Admin adminBuild = Admin.newBuilder
    Admin admin = Admin.newBuilder()
                  .setId(rs.getString("id"))
                  .build();
    return admin;
  }
}
