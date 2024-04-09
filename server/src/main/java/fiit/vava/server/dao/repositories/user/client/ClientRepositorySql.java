package fiit.vava.server.dao.repositories.user.client;

import fiit.vava.server.Client;
import fiit.vava.server.infrastructure.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepositorySql extends ClientRepository {

    @Override
    public List<Client> getNonConfirmedClients() {
        List<Client> nonConfirmedClients = new ArrayList<>();
        String sql = "SELECT * FROM \"Client\" JOIN \"User\" ON \"Client\".user_id = \"User\".id WHERE \"User\".confirmed = false";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                nonConfirmedClients.add(mapRowToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nonConfirmedClients;
    }

    @Override
    public Client save(Client toSave) {
        // This method assumes you are updating the Client details. Adjust accordingly for insert vs. update.
        String sql = "UPDATE \"Client\" SET registration_date = ?, first_name = ?, last_name = ?, date_of_birth = ?, region_in_new_country = ?, country = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            // Assuming the Client class has getter methods for these properties.
            pstmt.setDate(1, Date.valueOf(toSave.getRegistrationDate()));
            pstmt.setString(2, toSave.getFirstName());
            pstmt.setString(3, toSave.getLastName());
            pstmt.setDate(4, Date.valueOf(toSave.getDateOfBirth()));
            pstmt.setString(5, toSave.getRegion());
            pstmt.setString(6, toSave.getCountry());
            pstmt.setString(7, toSave.getId());
            pstmt.executeUpdate();
            return toSave;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM \"Client\"";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                clients.add(mapRowToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Client findById(String id) {
        String sql = "SELECT * FROM \"Client\" WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRowToClient(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Client mapRowToClient(ResultSet rs) throws SQLException {
        // Assuming the existence of a builder or constructor for Client that matches these properties
        Client client = Client.newBuilder()
                .setId(rs.getString("id"))
                // Set other fields from the ResultSet
                .build();
        return client;
    }
}
