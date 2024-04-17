package fiit.vava.server.dao.repositories.user.coworker;

import fiit.vava.server.Coworker;
import fiit.vava.server.infrastructure.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoworderRepositorySql extends CoworkerRepository {

  @Override
  public Coworker save(Coworker toSave) {
    String sql = toSave.getId() == null ?
                 "INSERT INTO \"Coworker\" (user_id, created_by, prefered_countries) VALUES (?, ?, ?) RETURNING id" :
                 "UPDATE \"Coworker\" SET user_id = ?, created_by = ?, prefered_countries = ? WHERE id = ? RETURNING id";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
      pstmt.setObject(1, UUID.fromString(toSave.getUser().getId()));
      pstmt.setObject(2, UUID.fromString(toSave.getCreatedBy()));
      pstmt.setArray(3, conn.createArrayOf("text", toSave.getPreferredCountriesList().toArray(new String[0])));
      if (toSave.getId() != null) {
        pstmt.setObject(4, UUID.fromString(toSave.getId()));
      }
      int affectedRows = pstmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Creating/updating coworker failed, no rows affected.");
      }
      Coworker.Builder coworkerBuilder = Coworker.newBuilder(toSave);
      try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          coworkerBuilder.setId(generatedKeys.getString(1));
        } else {
          throw new SQLException("Creating coworker failed, no ID obtained.");
        }
      }
      return coworkerBuilder.build();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Coworker findById(String id) {
    String sql = "SELECT * FROM \"Coworker\" WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setObject(1, UUID.fromString(id));
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapRowToCoworker(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Coworker> findAll() {
    List<Coworker> coworkers = new ArrayList<>();
    String sql = "SELECT * FROM \"Coworker\"";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql);
           ResultSet rs = pstmt.executeQuery()) {
      while (rs.next()) {
        coworkers.add(mapRowToCoworker(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return coworkers;
  }

  private Coworker mapRowToCoworker(ResultSet rs) throws SQLException {
    Coworker.Builder coworker = Coworker.newBuilder()
                                .setId(rs.getString("id"))
                                .setCreatedBy(rs.getString("created_by"));
    // Assume User is a simple field that requires complex join or secondary query
    // Preferably handled outside of this mapping for simplicity or set as a simple id reference
    String[] preferredCountries = (String[]) rs.getArray("prefered_countries").getArray();
    coworker.addAllPreferredCountries(List.of(preferredCountries));
    return coworker.build();
  }
}
