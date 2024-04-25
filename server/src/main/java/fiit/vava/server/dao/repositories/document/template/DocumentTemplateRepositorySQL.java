package fiit.vava.server.dao.repositories.document.template;

import fiit.vava.server.DocumentTemplate;
import fiit.vava.server.DocumentTemplateType;
import fiit.vava.server.dao.repositories.ToImplement;
import fiit.vava.server.infrastructure.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentTemplateRepositorySQL extends DocumentTemplateRepository {

    @Override
    public DocumentTemplate save(DocumentTemplate toSave) {
        String sql = toSave.getId() == null ?
                "INSERT INTO \"Template\" (name, file_path, type) VALUES (?, ?, ?::\"DocumentTemplateType\") RETURNING id" :
                "UPDATE \"Template\" SET name = ?, file_path = ?, type = ?::\"DocumentTemplateType\" WHERE id = ? RETURNING id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, toSave.getName());
            pstmt.setString(2, toSave.getPath());
            pstmt.setString(3, toSave.getType().name());

            if (toSave.getId() != null) {
                pstmt.setObject(4, UUID.fromString(toSave.getId()));
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating/updating template failed, no rows affected.");
            }

            DocumentTemplate.Builder templateBuilder = DocumentTemplate.newBuilder(toSave);
            if (toSave.getId() == null) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        templateBuilder.setId(generatedKeys.getString(1));
                    } else {
                        throw new SQLException("Creating template failed, no ID obtained.");
                    }
                }
            }
            return templateBuilder.build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DocumentTemplate findById(String id) {
        String sql = "SELECT * FROM \"Template\" WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, UUID.fromString(id));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToDocumentTemplate(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DocumentTemplate mapRowToDocumentTemplate(ResultSet rs) throws SQLException {
        return DocumentTemplate.newBuilder()
                .setId(rs.getString("id"))
                .setName(rs.getString("name"))
                .setPath(rs.getString("file_path"))
                .setType(DocumentTemplateType.valueOf(rs.getString("type")))
                .build();
    }

    @Override
    public List<DocumentTemplate> findAll() {
        List<DocumentTemplate> templates = new ArrayList<>();
        String sql = "SELECT * FROM \"Template\"";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                templates.add(mapRowToDocumentTemplate(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return templates;
    }

    @Override
    @ToImplement
    public DocumentTemplate getClientPassportTemplate() {
        return null;
    }
}
