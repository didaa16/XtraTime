package services;

import entities.sponso;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class sponsoService implements IService<sponso> {
    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;


    public sponsoService() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(sponso sponso) {
        String req = "INSERT INTO sponso (nom,tel,email,image) " +
                "VALUES ('" + sponso.getNom() + "',  " + sponso.getTel() + ", '" + sponso.getEmail() + "', '" + sponso.getImage() + "')";

        try {
            ste = conn.createStatement();
            ste.executeUpdate(req);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(sponso sponso) {
        String req = "DELETE FROM sponso WHERE idsponso = " + sponso.getIdsponso();
        try {
            ste = conn.createStatement();
            ste.executeUpdate(req);
            System.out.println("sponso supp");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(sponso sponso) {
        String req = "UPDATE sponso SET nom = ?, tel = ?, email = ?, image = ? WHERE idsponso = ?";
        try {
            pst = conn.prepareStatement(req);
            pst.setString(1, sponso.getNom());
            pst.setInt(2, sponso.getTel());
            pst.setString(3, sponso.getEmail());
            pst.setString(4, sponso.getImage());
            pst.setInt(5, sponso.getIdsponso());
            pst.executeUpdate();
            System.out.println("Sponso updated successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<sponso> readAll() {
        List<sponso> sponsos = new ArrayList<>();
        String req = "SELECT * FROM sponso";
        try {
            ste = conn.createStatement();
            ResultSet resultSet = ste.executeQuery(req);
            while (resultSet.next()) {
                sponso s = new sponso(
                        resultSet.getInt("idsponso"),
                        resultSet.getString("nom"),
                        resultSet.getInt("tel"),
                        resultSet.getString("email"),
                        resultSet.getString("image")
                );
                sponsos.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sponsos;
    }

    @Override
    public sponso readById(int id) {
        String req = "SELECT * FROM sponso WHERE idsponso = ?";
        try {
            pst = conn.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                return new sponso(
                        resultSet.getInt("idsponso"),
                        resultSet.getString("nom"),
                        resultSet.getInt("tel"),
                        resultSet.getString("email"),
                        resultSet.getString("image")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int getSponsorIdByName(String name) {
        String req = "SELECT idsponso FROM sponso WHERE nom = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(req);
            pst.setString(1, name);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("idsponso");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }


    public int numbersponso() throws SQLException {
        int y = 0;
        this.ste = this.conn.createStatement();

        for(ResultSet rs = this.ste.executeQuery("SELECT COUNT(*) as total FROM sponso "); rs.next(); y = rs.getInt("total")) {
        }

        System.out.println("total number : " + y);
        return y;
    }
    public boolean sponsorExistsByName(String nom) {
        String req = "SELECT * FROM sponso WHERE nom = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(req);
            ps.setString(1, nom);
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

    public boolean sponsorExistsByTel(int tel) {
        String req = "SELECT * FROM sponso WHERE tel = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(req);
            ps.setInt(1, tel);
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
    public boolean imageExists(String imagePath) {
        String req = "SELECT * FROM event WHERE image = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(req);
            ps.setString(1, imagePath);
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


    public boolean sponsorExistsByEmail(String email) {
        String req = "SELECT * FROM sponso WHERE email = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(req);
            ps.setString(1, email);
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
}
