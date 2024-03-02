package services;

import entities.*;
import utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommandeProduit implements IService<Produit_Commande> {
    private Connection connection;

    public ServiceCommandeProduit() {
        this.connection = DataBase.getInstance().getConnection();
    }


    /*public void ajouter1(Produit_Commande produitCommande) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String req = "INSERT INTO `produitCommande` (`ref`, `refCommande`) VALUES (?,?);";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, produitCommande.getRef());
        ps.setInt(2, produitCommande.getRefCommande());
        ps.executeUpdate();
    }*/
//    public void delete(Produit_Commande produitCommande) throws SQLException {
//        String req = "DELETE FROM `produitCommande` WHERE `ref`= ? AND `refCommande`= ?" ;
//        PreparedStatement ps = connection.prepareStatement(req);
//        ps.setString(1, produitCommande.getRef());
//        ps.setInt(2, produitCommande.getRefCommande());
//        ps.executeUpdate();
//    }

    @Override
    public List<Produit_Commande> afficher() throws SQLException {
        return null;
    }

    @Override
    public void ajouter(Produit_Commande produitCommande) throws SQLException {
        String req = "INSERT INTO `produit_commande`(`ref`, `refCommande`) VALUES (?,?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, produitCommande.getRef());
        ps.setInt(2, produitCommande.getRefCommande());
        ps.executeUpdate();
        System.out.println("Commande Produit Ajoutée");
    }

    @Override
    public  void modifier(Produit_Commande produitCommande) throws SQLException {
        String req = "UPDATE  `produitCommande` SET `ref`= ?, `refCommande`= ? WHERE `id`= ?;";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, produitCommande.getRef());
        ps.setInt(2, produitCommande.getRefCommande());
        ps.executeUpdate();
        System.out.println("Commande Produit Modifiée");
    }

    @Override
    public void supprimer(String ref) throws SQLException {

    }

    public void delete(int refCom, String ref) throws SQLException {
        String req = "DELETE FROM `produit_commande` WHERE `refCommande`= ?";
        PreparedStatement ps= connection.prepareStatement(req);
        ps.setInt(1, refCom);
        ps.executeUpdate();
        System.out.println("commande produit Supprimée");
    }
    public void deleteProduitFromCommande(String ref, int refCommande) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection(); // Obtenir une nouvelle connexion
        String req = "DELETE FROM produit_commande WHERE ref = ? AND refCommande = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, ref);
        ps.setInt(2, refCommande);
            ps.executeUpdate();
            System.out.println("commande produit Supprimée");

    }






    public List<String> getProdId() throws SQLException {
        String req= "Select DISTINCT ref from `produitCommande` ";
        List<String> listeP=new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            listeP.add(String.valueOf(rs.getString("ref")));
            System.out.println(listeP.isEmpty());
        }
        return listeP;
    }
    public static List<Produit> getProduitsByRefCommande(String refCommande) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection(); // Obtenir une nouvelle connexion

        List<Produit> produits = new ArrayList<>();
        String req = "SELECT p.* FROM produit_commande pc INNER JOIN produit p ON pc.ref = p.ref WHERE pc.refCommande = ?";

        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, refCommande);
        ResultSet rs = ps.executeQuery();
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



    public List<Produit> listerP(String refCommande) throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT p.* FROM produit_commande pc INNER JOIN produit p ON pc.ref = p.ref WHERE pc.refCommande = ?";
        this.connection= DataBase.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, refCommande);
        ResultSet rs = ps.executeQuery();
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


    public void ajouterProduitACommande(int refCommande, String ref) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection(); // Obtenir une nouvelle connexion

        // Vérifiez si la commande existe, sinon créez-la
        if (!verifierExistenceCommande(refCommande)) {
            Commande commande = new Commande(refCommande, 0.0, Status.enAttente, "dida16");
            ServiceCommande serviceCommande = new ServiceCommande();
            serviceCommande.ajouter(commande);
        }

        String req = "insert into produit_commande (ref, refCommande) values(?, ?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, ref);
        ps.setInt(2, refCommande);
        ps.executeUpdate();
        System.out.println("Produit ajouté à la commande");
    }

    private boolean verifierExistenceCommande(int refCommande) throws SQLException {
        String req = "SELECT COUNT(*) FROM commande WHERE refCommande = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, refCommande);
        ResultSet rs = ps.executeQuery();
        rs.next(); // Déplace le curseur sur la première ligne de résultats
        return rs.getInt(1) > 0;
    }


    public double getPrixTotalCommande(int refCommande) throws SQLException {
        List<Produit> produits = listerP(String.valueOf(refCommande));
        double prixTotal = 0.0;
        for (Produit produit : produits) {
            prixTotal += produit.getPrix();
        }
        return prixTotal;
    }





}
