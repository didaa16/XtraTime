package services;

import entities.Equipement;
import entities.Reservation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements  IService<Reservation>{
    Connection connection;

    public ServiceReservation(){
        connection= MyDatabase.getInstance().getConnection();

    }
    @Override
    public void ajouter(Reservation res) throws SQLException {
        String req ="insert into reservation (id, terrainid, clientPseudo , equipements ,prix ,date ,duree)"+
                "values('"+res.getId()+"','"+res.getTerrainid()+"','"+res.getClientPseudo()+"','"+res.getEquipements()+"','"+res.getPrix()+"','"+res.getDate()+"',"+res.getDuree()+")";
        Statement st = connection.createStatement();
        st.executeUpdate(req);
        System.out.println("reservation ajouté");
    }


    @Override
    public void modifier(Reservation res) throws SQLException {
        String req="update reservation set  terrainid=? , clientPseudo=? ,equipements=? ,prix=? ,date=? ,duree=?  where id=?";
        PreparedStatement ps= connection.prepareStatement(req);
        ps.setInt(1, res.getTerrainid());
        ps.setString(2, res.getClientPseudo());
        ps.setString(3, res.getEquipements());
        ps.setInt(4, res.getPrix());
        ps.setString(5, res.getDuree());
        ps.setString(6, res.getDuree());
        ps.setInt(7, res.getId());
        ps.executeUpdate();
        System.out.println("reservation modifie");

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req ="delete from reservation where id = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1,id);
        ps.executeUpdate();
        System.out.println("supp avec succes !!");

    }

    @Override
    public List<Reservation> afficher() throws SQLException {

        List<Reservation> reservations= new ArrayList<>();
        String req="select * from reservation";
        Statement st  = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()){
            Reservation r = new Reservation();
            r.setId(rs.getInt(1));
            r.setDate(rs.getString("date"));
            r.setDuree(rs.getString("duree"));
            r.setPrix(rs.getInt("prix"));
            r.setTerrainid(rs.getInt("terrainId"));
            r.setClientPseudo(rs.getString("clientPseudo"));
            r.setEquipements(rs.getString("equipements"));

            reservations.add(r);
        }
        return reservations;
    }
}
