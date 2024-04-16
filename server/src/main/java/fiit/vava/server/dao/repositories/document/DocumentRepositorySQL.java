package fiit.vava.server.dao.repositories.document;

import fiit.vava.server.Document;
import fiit.vava.server.infrastructure.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentRepositorySQL extends DocumentRepository {

  @Override
  public Document save(Document toSave) {
    String sql = toSave.getId() == null ?
                 "INSERT INTO \"Document\" (name, path) VALUES (?, ?)" :
                 "UPDATE \"Document\" SET name = ?, path = ? WHERE id = ?";

    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
      pstmt.setString(1, toSave.getName());
      pstmt.setString(2, toSave.getPath());

      if (toSave.getId() != null) {
        pstmt.setObject(3, UUID.fromString(toSave.getId()));
      }

      int affectedRows = pstmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Creating/updating document failed, no rows affected.");
      }

      Document.Builder docBuilder = Document.newBuilder(toSave);
      if (toSave.getId() == null) {
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            docBuilder.setId(generatedKeys.getString(1));
          } else {
            throw new SQLException("Creating document failed, no ID obtained.");
          }
        }
      }
      return docBuilder.build();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Document> findAll() {
    List<Document> documents = new ArrayList<>();
    String sql = "SELECT * FROM \"Document\"";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql);
           ResultSet rs = pstmt.executeQuery()) {
      while (rs.next()) {
        documents.add(mapRowToDocument(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return documents;
  }

  @Override
  public Document findById(String id) {
    String sql = "SELECT * FROM \"Document\" WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setObject(1, UUID.fromString(id));
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapRowToDocument(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Document mapRowToDocument(ResultSet rs) throws SQLException {
    Document document = Document.newBuilder()
                        .setId(rs.getString("id"))
                        .setName(rs.getString("name"))
                        .setPath(rs.getString("path"))
                        .build();
    return document;
  }
}
