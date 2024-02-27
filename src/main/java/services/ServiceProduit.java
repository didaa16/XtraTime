package services;
import entities.Marque;
import entities.Produit;
import entities.TypeSport;
import utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduit implements IService<Produit>{

    Connection connection;
    public ServiceProduit(){
        connection= DataBase.getInstance().getConnection();

    }
   @Override
    public void ajouter(Produit produit) throws SQLException {

       String req ="insert into produit (ref,  nom, description, prix, marque, taille, typeSport, quantite , image)"+
               "values('"+produit.getRef()+"','"+produit.getNom()+"','"+produit.getDescription()+"',"+produit.getPrix()+",'"+produit.getMarque()+"','"+produit.getTaille()+"','"+produit.getTypeSport()+"','"+produit.getQuantite()+"','"+produit.getImage()+"')";

       Statement st;
        st = connection.createStatement();
        st.executeUpdate(req);
        System.out.println("produit ajouté");
    }


    @Override
    public void modifier(Produit produit) throws SQLException {
        String req = "update produit set nom=?, description=?, prix=?, marque=?, taille=?, typeSport=?, quantite=?, image=? where ref=?";
        PreparedStatement ps = connection.prepareStatement(req);

        ps.setString(1, produit.getNom());
        ps.setString(2, produit.getDescription());
        ps.setDouble(3, produit.getPrix());
        ps.setString(4, produit.getMarque().toString());
        ps.setString(5, produit.getTaille());
        ps.setString(6, produit.getTypeSport().toString());
        ps.setInt(7, produit.getQuantite());
        ps.setString(8, produit.getImage());
        ps.setString(9, produit.getRef());
        ps.executeUpdate();
        System.out.println("Produit modifié");
    }


    @Override
    public void supprimer(String ref) throws SQLException {
        String req = "DELETE FROM produit WHERE ref = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, ref);
        ps.executeUpdate();
        System.out.println("Produit supprimé");
    }

    @Override
    public List<Produit> afficher() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT * FROM produit";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Produit produit = new Produit();
            produit.setRef(rs.getString("ref"));
            produit.setNom(rs.getString("nom"));
            produit.setDescription(rs.getString("description"));
            produit.setPrix(rs.getDouble("prix"));
            produit.setMarque(Marque.valueOf(rs.getString("marque")));
            produit.setTaille(rs.getString("taille"));
            produit.setTypeSport(TypeSport.valueOf(rs.getString("typeSport")));
            produit.setQuantite(rs.getInt("quantite"));
            produit.setImage(rs.getString("image"));
            produits.add(produit);
        }
        return produits;


    }
}

