package services;

import entities.*;
import utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommande implements IService<Commande> {


    @Override
    public void ajouter(Commande commande) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String req = "insert into commande (refCommande, prix, status, idUser)" +
                "values("+commande.getRefCommande()+","+commande.getPrix()+",'"+commande.getStatus()+"','"+commande.getIdUser()+"')";
        Statement st;
        st = connection.createStatement();
        st.executeUpdate(req);
        System.out.println("Commande ajoutée");
    }

    public void ajouter2(Commande commande) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String req = "insert into commande (prix, status, idUser)" +
                "values( ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setDouble(1, commande.getPrix());
        ps.setString(2, commande.getStatus().toString());
        ps.setString(3, commande.getIdUser());
        ps.executeUpdate();
    }



    @Override
    public void modifier(Commande commande) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String req = "update commande set prix=?, status=?, idUser=? where refCommande=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setDouble(1, commande.getPrix());
        ps.setString(2, commande.getStatus().toString());
        ps.setString(3, commande.getIdUser());
        ps.setInt(4, commande.getRefCommande());
        ps.executeUpdate();
        System.out.println("Commande modifiée");
    }

    @Override
    public void supprimer(String refCommande) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String req = "delete from commande where refCommande=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, refCommande);
        ps.executeUpdate();
        System.out.println("Commande supprimée");
    }


    /*public void delete(Commande commande) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String req = ("DELETE FROM `commande` WHERE `refCommande`= ?");
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1,commande.getRefCommande());
    }*/




    @Override
    public List<Commande> afficher() throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        List<Commande> listCom=new ArrayList<>();
        String req = "select * from commande";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Commande commande = new Commande();
            commande.setRefCommande(rs.getInt("refCommande"));
            commande.setPrix(rs.getDouble("prix"));
            commande.setStatus(Status.valueOf(rs.getString("status")));
            commande.setIdUser(rs.getString("idUser"));
            listCom.add(commande);
        }
        return listCom;
    }

    public List<String> getStatus() throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String req = "Select DISTINCT status from `commande` ";
        List<String> cls=new ArrayList<>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while(rs.next())
        {
            cls.add(String.valueOf(Status.valueOf(rs.getString("status"))));
            System.out.println(cls.isEmpty());
        }
        return cls;
    }

    public int Count() throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String query = "SELECT COUNT(*) FROM commande";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        System.out.println("Nombre de commandes : " + count); // Ajoutez cette ligne pour afficher le nombre de commandes dans la console
        return count;
    }
    public int countByStatus(Status status) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String query = "SELECT COUNT(*) FROM commande WHERE status = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, status.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count;
    }
    public int countEnAttente() throws SQLException {
        return countByStatus(Status.enAttente);
    }

    public int countEnCours() throws SQLException {
        return countByStatus(Status.enCours);
    }

    public int countLivre() throws SQLException {
        return countByStatus(Status.livrée);
    }

    public Commande getCommande (String idUser) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String req = "SELECT * FROM `commande` WHERE `iduser` = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, idUser);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Commande cmd = new Commande();
            cmd.setRefCommande(rs.getInt("refCommande"));
            cmd.setPrix(rs.getDouble("prix"));
            cmd.setStatus(Status.valueOf(rs.getString("status")));
            cmd.setIdUser(rs.getString("iduser"));
            return cmd;
        }
        return null;
    }


    public boolean commandeExiste(Commande cmd) throws SQLException {
        if (cmd != null) {
            Connection connection = DataBase.getInstance().getConnection();
            String req = "SELECT * FROM `commande` WHERE `iduser` = ?";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, cmd.getIdUser());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } else {
            return false;
        }

    }

    public void updatePrixCommande(int refCommande, double nouveauPrix) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        String query = "UPDATE commande SET prix = ? WHERE refCommande = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDouble(1, nouveauPrix);
        preparedStatement.setString(2, String.valueOf(refCommande));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }








    /*
    @Override
    public List<Commande> afficher() throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        List<Commande> commandes = new ArrayList<>();
        String req = "select * from commande";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Commande commande = new Commande();
            commande.setRefCommande(rs.getString("refCommande"));
            commande.setPrix(rs.getDouble("prix"));
            commande.setStatus(Status.valueOf(rs.getString("status")));
            commande.setIdUser(rs.getString("idUser"));

            List<Produit> produits = new ArrayList<>();
            String reqProduits = "select * from produit where refCommande=?";
            PreparedStatement psProduits = connection.prepareStatement(reqProduits);
            psProduits.setString(1, commande.getRefCommande());
            ResultSet rsProduits = psProduits.executeQuery();
            while (rsProduits.next()) {
                Produit produit = new Produit();
                produit.setRef(rsProduits.getString("ref"));
                produit.setNom(rsProduits.getString("nom"));
                produit.setDescription(rsProduits.getString("description"));
                produit.setPrix(rsProduits.getDouble("prix"));
                produit.setMarque(Marque.valueOf(rsProduits.getString("marque")));
                produit.setTaille(rsProduits.getString("taille"));
                produit.setTypeSport(TypeSport.valueOf(rsProduits.getString("typeSport")));
                produit.setQuantite(rsProduits.getInt("quantite"));
                produit.setImage(rsProduits.getString("image"));
                produits.add(produit);
            }
            commande.setProduits(produits);
            commandes.add(commande);
        }
        return commandes;
    }*/

   /* public void ajouterProduitACommande(String refCommande, Produit produit) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();

        // Vérifiez si la commande existe, sinon créez-la
        if (!verifierExistenceCommande(refCommande)) {
            Commande commande = new Commande(refCommande, 0.0, Status.waiting, "idUser");
            ajouter(commande);
        }

        */



}
