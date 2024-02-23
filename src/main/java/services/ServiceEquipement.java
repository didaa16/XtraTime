package services;

import entities.Equipement;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEquipement implements  IService<Equipement>{
    Connection connection;

    public ServiceEquipement(){
        connection= MyDatabase.getInstance().getConnection();

    }
    @Override
    public void ajouter(Equipement equi) throws SQLException {
        String req ="insert into equipement (nom, description , type ,prix ,image , stock)"+
                "values('"+equi.getNom()+"','"+equi.getDescription()+"','"+equi.getType()+"','"+equi.getPrix()+"','"+equi.getImage()+"',"+equi.getStock()+")";
        Statement st = connection.createStatement();
        st.executeUpdate(req);
        System.out.println("equipement ajouté");
    }



    @Override
    public void modifier(Equipement equi) throws SQLException {
        String req="update equipement set nom=? , description=? ,type=? ,prix=? ,image=? ,stock=? where idEquipement=?";
        PreparedStatement ps= connection.prepareStatement(req);
        ps.setString(1, equi.getNom());
        ps.setString(2, equi.getDescription());
        ps.setString(3, equi.getType());
        ps.setInt(4, equi.getPrix());
        ps.setString(5, equi.getImage());
        ps.setInt(6, equi.getStock());
        ps.setInt(7, equi.getId());
        ps.executeUpdate();
        System.out.println("Equipement modifie");

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req ="delete from equipement where idEquipement = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1,id);
        ps.executeUpdate();
        System.out.println("supp avec succes !!");

    }

    @Override
    public List<Equipement> afficher() throws SQLException {

        List<Equipement> equipements= new ArrayList<>();
        String req="select * from equipement";
        Statement st  = connection.createStatement();
       ResultSet rs = st.executeQuery(req);
       while (rs.next()){
           Equipement p = new Equipement();
           p.setId(rs.getInt(1));
           p.setNom(rs.getString("nom"));
           p.setDescription(rs.getString(3));
           p.setType(rs.getString("type"));
           p.setPrix(rs.getInt("prix"));
           p.setImage(rs.getString("image"));
           p.setStock(rs.getInt("stock"));

           equipements.add(p);
       }
        return equipements;
    }

    public List<Equipement> recherche(String nom) throws SQLException {

        List<Equipement> equipements= new ArrayList<>();
        String req="select * from equipement WHERE nom = '"+nom+"' ";
        Statement st  = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()){
            Equipement p = new Equipement();
            p.setId(rs.getInt(1));
            p.setNom(rs.getString("nom"));
            p.setDescription(rs.getString(3));
            p.setType(rs.getString("type"));
            p.setPrix(rs.getInt("prix"));
            p.setImage(rs.getString("image"));
            p.setStock(rs.getInt("stock"));

            equipements.add(p);
        }
        return equipements;
    }

}
