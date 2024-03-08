package services.Reservation;

import entities.Reservation.Equipement;
import utils.MyDatabase;

import javax.swing.*;
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
        String checkReq = "SELECT COUNT(*) FROM equipement WHERE nom = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkReq);
        checkStatement.setString(1, equi.getNom());
        ResultSet resultSet = checkStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);

        if (count > 0) {
            JOptionPane.showMessageDialog(null,"Erreur : L'équipement existe déjà dans la base de données !");
        } else {
            String req ="INSERT INTO equipement (nom, description, type, prix, image, stock, terrainId)"+
                    " VALUES (?, ?, ?, ?, ?, ? , ?)";
            PreparedStatement st = connection.prepareStatement(req);
            st.setString(1, equi.getNom());
            st.setString(2, equi.getDescription());
            st.setString(3, equi.getType());
            st.setInt(4, equi.getPrix());
            st.setString(5, equi.getImage());
            st.setInt(6, equi.getStock());
            st.setInt(7, equi.getTerrainIdAjout());

            st.executeUpdate();
            JOptionPane.showMessageDialog(null,"Equipement Ajoutée avec succés !");
        }
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

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req ="delete from equipement where idEquipement = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1,id);
        ps.executeUpdate();

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


    public List<Equipement> getEquipementsByTerrainId(int idTerrain){
        List<Equipement> equipements= new ArrayList<>();
        String req="select * from `equipement` WHERE `terrainId` = "+idTerrain+" ";
        try {
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
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return equipements;
    }

    public List<Equipement>getEquipementByNom(String nom){
        List<Equipement> equipements= new ArrayList<>();
        String req="select * from `equipement` WHERE `nom` = "+nom+" ";
        try {
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
        }catch (SQLException e){
            System.out.println(e.getMessage());
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
