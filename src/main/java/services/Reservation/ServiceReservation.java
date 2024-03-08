package services.Reservation;

import entities.Reservation.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.MyDatabase;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import controllers.Reservation.mesReservations;

public class ServiceReservation implements IService<Reservation> {
    static Connection connection;

    public ServiceReservation() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Reservation res) throws SQLException {
        String req = "INSERT INTO `reservation`(`date`, `duree`, `prix`, `terrainId`, `clientPseudo`, `equipements`)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, res.getDate());
        ps.setString(2, res.getDuree());
        ps.setInt(3, res.getPrix());
        ps.setInt(4, res.getTerrainId());
        ps.setString(5, res.getClientPseudo());
        ps.setString(6, res.getEquipements());

        ps.executeUpdate();
    }

    @Override
    public void modifier(Reservation res) throws SQLException {
        String req = "UPDATE reservation SET terrainid=?, clientPseudo=?, equipements=?, prix=?, date=?, duree=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, res.getTerrainId());
        ps.setString(2, res.getClientPseudo());
        ps.setString(3, res.getEquipements());
        ps.setInt(4, res.getPrix());
        ps.setString(5, res.getDate());
        ps.setString(6, res.getDuree());
        ps.setInt(7, res.getId());
        ps.executeUpdate();
        System.out.println("Réservation modifiée");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `reservation` WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Réservation supprimée avec succès");
    }


    public List<Reservation> afficherParPseudo(String pseudo) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation WHERE clientPseudo = '"+pseudo+"'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Reservation r = new Reservation();
            r.setId(rs.getInt("id"));
            r.setTerrainId(rs.getInt("terrainid"));
            r.setClientPseudo(rs.getString("clientPseudo"));
            r.setEquipements(rs.getString("equipements"));
            r.setPrix(rs.getInt("prix"));
            r.setDate(rs.getString("date"));
            r.setDuree(rs.getString("duree"));
            reservations.add(r);
        }
        return reservations;
    }

    public Reservation afficherParId(int id) throws SQLException {
        Reservation r = new Reservation();
        String req = "SELECT * FROM reservation WHERE id = '"+ id +"' ";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            r.setId(rs.getInt("id"));
            r.setTerrainId(rs.getInt("terrainid"));
            r.setClientPseudo(rs.getString("clientPseudo"));
            r.setEquipements(rs.getString("equipements"));
            r.setPrix(rs.getInt("prix"));
            r.setDate(rs.getString("date"));
            r.setDuree(rs.getString("duree"));
        }
        return r;
    }

    public List<Reservation> afficher() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Reservation r = new Reservation();
            r.setId(rs.getInt("id"));
            r.setTerrainId(rs.getInt("terrainid"));
            r.setClientPseudo(rs.getString("clientPseudo"));
            r.setEquipements(rs.getString("equipements"));
            r.setPrix(rs.getInt("prix"));
            r.setDate(rs.getString("date"));
            r.setDuree(rs.getString("duree"));
            reservations.add(r);
        }
        return reservations;
    }
    public void changeScreen(ActionEvent event, String fxmlFile, String title){
        try {
            FXMLLoader loader = new FXMLLoader(mesReservations.class.getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}