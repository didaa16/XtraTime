package services;

import entities.event;
import utils.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class eventService implements IService<event>{
    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;

    public eventService() {
        conn= DataSource.getInstance().getCnx();
    }

    @Override
    public void add(event event) {
        String req = "INSERT INTO event (titre,description,image, datedebut, datefin, idterrain, idsponso,iduser) " +
                "VALUES ('" + event.getTitre() + "','" + event.getDescription() + "','" + event.getImage() + "'," +
                " '" + event.getDatedebut() + "', '" + event.getDatefin() + "', " + event.getIdterrain() + ", " + event.getIdsponso() + ",'" + event.getIduser()+"')";

        try {
            ste=conn.createStatement();
            ste.executeUpdate(req);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(event event) {
        String req = "DELETE FROM event WHERE idevent = " + event.getIdevent();
        try {
            ste=conn.createStatement();
            ste.executeUpdate(req);
            System.out.println("event supp");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(event event) {
        String req = "UPDATE event SET titre = ?,description=?,image=?, datedebut = ?, " +
                "datefin = ?, idterrain = ?, idsponso = ? ,iduser=? WHERE idevent = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(req);
            ps.setString(1, event.getTitre());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getImage());
            ps.setTimestamp(4, event.getDatedebut());
            ps.setTimestamp(5, event.getDatefin());
            ps.setInt(6, event.getIdterrain());
            ps.setInt(7, event.getIdsponso());
            ps.setString(8, event.getIduser());
            ps.setInt(9, event.getIdevent());
            ps.executeUpdate();


            System.out.println("Event updated successfully");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<event> readAll() {
        List<event> events = new ArrayList<>();
        String req = "SELECT * FROM event";
        try {


            ste = conn.createStatement();
            ResultSet resultSet = ste.executeQuery(req);
            while (resultSet.next()) {



                event event = new event(
                        resultSet.getInt("idevent"),
                        resultSet.getString("titre"),
                        resultSet.getString("description"),
                        resultSet.getString("image"),
                        resultSet.getTimestamp("datedebut"),
                        resultSet.getTimestamp("datefin"),
                        resultSet.getInt("idterrain"),
                        resultSet.getInt("idsponso"),
                        resultSet.getString("iduser")
                );
                events.add(event);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    @Override
    public event readById(int id) {
        String req = "SELECT * FROM event WHERE idevent = ?";
        try {
            pst = conn.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                return new event(
                        resultSet.getInt("idevent"),
                        resultSet.getString("titre"),
                        resultSet.getString("description"),
                        resultSet.getString("image"),
                        resultSet.getTimestamp("datedebut"),
                        resultSet.getTimestamp("datefin"),
                        resultSet.getInt("idterrain"),
                        resultSet.getInt("idsponso"),
                        resultSet.getString("iduser")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Return null if event with given id is not found

    }
    public int getterrainIdByName(String name) {
        String req = "SELECT id FROM terrain WHERE nom = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(req);
            pst.setString(1, name);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
    public String getNomsponsoByID(int id) {
        String req = "SELECT nom FROM sponso WHERE idsponso = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nom");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }
    public String getNomterrainIdByID(int id) {
        String req = "SELECT nom FROM terrain WHERE id = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nom");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }
    public int numberevent() throws SQLException {
        int y = 0;
        this.ste = this.conn.createStatement();

        for(ResultSet rs = this.ste.executeQuery("SELECT COUNT(*) as total FROM event "); rs.next(); y = rs.getInt("total")) {
        }

        System.out.println("total number : " + y);
        return y;
    }

    public int findbyImage(String image) {
        int u = 0;

        try {
            PreparedStatement pre = this.conn.prepareStatement("Select * from event  WHERE image=? ");
            pre.setString(1, image);

            for(ResultSet rs = pre.executeQuery(); rs.next(); u = rs.getInt("idevent")) {
            }
        } catch (SQLException var5) {
            var5.getMessage();
        }

        return u;
    }

    public boolean eventExistsWithSameDates(event event) {
        String req = "SELECT * FROM event WHERE datedebut = ? AND datefin = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(req);
            ps.setTimestamp(1, event.getDatedebut());
            ps.setTimestamp(2, event.getDatefin());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

        public Map<Timestamp, List<event>> getEventsByDateRange(Timestamp startDate, Timestamp endDate) throws SQLException {
            Map<Timestamp, List<event>> eventsByDate = new HashMap<>();

            String sql = "SELECT * FROM event WHERE datedebut >= ? AND datefin <= ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setTimestamp(1, (startDate));
                statement.setTimestamp(2, (endDate));
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Timestamp eventDate = resultSet.getTimestamp("datedebut");
                        event event = new event(

                                resultSet.getInt("idevent"),
                                resultSet.getString("titre"),
                                resultSet.getString("description"),
                                resultSet.getString("image"),
                                eventDate,
                                resultSet.getTimestamp("datefin"),
                                resultSet.getInt("idterrain"),
                                resultSet.getInt("idsponso"),
                                resultSet.getString("iduser")

                                // Ajoutez d'autres colonnes de la base de données selon votre schéma d'événements
                        );

                        eventsByDate.computeIfAbsent(eventDate, k -> new ArrayList<>()).add(event);
                    }
                }
            }

            return eventsByDate;
        }







}
