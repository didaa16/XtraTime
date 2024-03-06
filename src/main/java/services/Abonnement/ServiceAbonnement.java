package services.Abonnement;

import entities.Abonnement.Abonnement;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceAbonnement {
    Connection connection;
    private float prixTotalAvantReduction;

    public ServiceAbonnement() {
        connection = MyDatabase.getInstance().getConnection();
    }
/*
    public float calculerPrixAbonnement(int terrainId, int packId) throws SQLException {
        // Récupérer le prix du terrain
        float prixTerrain = getPrixTerrain(terrainId);

        // Récupérer la réduction du pack
        float reductionPack = getReductionPack(packId);

        // Calculer le prix de l'abonnement
        float prixAbonnement = prixTerrain * reductionPack;

        return prixAbonnement;
    }
    */

    public float calculerPrixApresReduction(float prixTotalAvantReduction, int packId, int terrainId, String nomPack) throws SQLException {
        // Obtenez la réduction du pack
       int reductionPack = getReductionPack(packId);
       float prix= calculerPrixTotalAvantReduction( nomPack,  terrainId,  packId);
        // Calculez le montant de la réduction en multipliant le prix total avant réduction par le pourcentage de réduction
        float montantReduction = prix * ((float)reductionPack/100);

        // Calculez le prix total après réduction en soustrayant le montant de la réduction du prix total avant réduction
        float prixTotalApresReduction = prix - montantReduction;

        return prixTotalApresReduction;
    }

    public float calculerPrixTotalAvantReduction(String nomPack,  int terrainId, int packId) throws SQLException {
        float prixTerrain = getPrixTerrain(terrainId);
        switch (nomPack.toLowerCase()) {
            case "premium":
                return prixTerrain * 180;
            case "standard":
                return prixTerrain * 90;
            case "basic":
                return prixTerrain * 30;
            default:
                // Si le nom du pack n'est pas reconnu, retourner le prix de base
                return prixTerrain;
        }
    }

    private float getPrixTerrain(int terrainId) throws SQLException {
        String req = "SELECT prix FROM terrain WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, terrainId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getFloat("prix");
            } else {
                throw new SQLException("Terrain introuvable avec l'ID spécifié: " + terrainId);
            }
        }
    }




    private int getReductionPack(int packId) throws SQLException {
        String req = "SELECT reduction FROM pack WHERE idP = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, packId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("reduction");
            } else {
                throw new SQLException("Pack introuvable avec l'ID spécifié");
            }
        }
    }

    public void ajouterAbonnement(String nomUser, LocalDate date, String nomTerrain, String nomPack, int numtel) throws SQLException {
        // Obtenir l'ID du terrain à partir de son nom
        int terrainId = getTerrainIdByNom(nomTerrain);

        // Obtenir l'ID du pack à partir de son nom
        int packId = getPackIdByNom(nomPack);

        // Calculer le prix de l'abonnement
        float prixTotalAvantReduction = calculerPrixTotalAvantReduction(nomPack ,terrainId, packId);
        float prixTotal = calculerPrixApresReduction(prixTotalAvantReduction ,packId , terrainId ,nomPack);

        // Insérer l'abonnement dans la base de données
        String req = "INSERT INTO abonnement (date, prix, nomterrain, nomPack, nomUser, numtel,prixTotal,terrainId,packId ) VALUES (?, ?, ?, ?, ?, ?, ?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            // Set parameters
            preparedStatement.setDate(1, java.sql.Date.valueOf(date)); // Conversion de LocalDate en java.sql.Date
            preparedStatement.setFloat(2, prixTotalAvantReduction);
            preparedStatement.setString(3, nomTerrain);
            preparedStatement.setString(4, nomPack);
            preparedStatement.setString(5, nomUser);
            preparedStatement.setInt(6, numtel);
            preparedStatement.setFloat(7, prixTotal);
            preparedStatement.setInt(8, terrainId);
            preparedStatement.setInt(9, packId);



            // Exécuter la requête
            preparedStatement.executeUpdate();
            System.out.println("Abonnement ajouté avec succès");
        }
    }

    // Autres méthodes inchangées...



    // Autres méthodes inchangées...

    public List<String> getAllTerrainIds() throws SQLException {
        List<String> terrainIds = new ArrayList<>();
        String req = "SELECT nom FROM terrain";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String Nomterain = resultSet.getString("nom");
                terrainIds.add(Nomterain);
            }
        }
        return terrainIds;
    }



    public List<String> getAllPackNames() throws SQLException {
        List<String> packNames = new ArrayList<>();
        String req = "SELECT nom FROM pack";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String packName = resultSet.getString("nom");
                packNames.add(packName);
            }
        }
        return packNames;
    }
/*
    public List<Abonnement> afficherAbonnements() {
        List<Abonnement> abonnements = new ArrayList<>();

        String req = "SELECT * FROM abonnement"; // Sélectionnez toutes les colonnes de la table abonnement
        try (PreparedStatement statement = connection.prepareStatement(req);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date date = resultSet.getDate("date");
                float prix = resultSet.getFloat("prix");
                String nomUser = resultSet.getString("nomUser");
                int numtel = resultSet.getInt("numtel");
                String nomPack = resultSet.getString("nomPack"); // Récupérez le nom du pack
                String nomTerrain = resultSet.getString("nomTerrain"); // Récupérez le nom du terrain

                // Obtenir l'ID du terrain à partir de son nom
                int terrainId = getTerrainIdByNom(nomTerrain);

                // Obtenir l'ID du pack à partir de son nom
                int packId = getPackIdByNom(nomPack);

                // Supposons que vous avez une méthode calculerPrixApresReduction qui fait le calcul
                float prixTotal = calculerPrixApresReduction(prixTotalAvantReduction, packId, terrainId, nomPack);

                // Créez un objet Abonnement avec les valeurs récupérées, y compris l'ID du terrain
                Abonnement abonnement = new Abonnement(id, date, prix,prixTotal,terrainId, packId, nomUser, nomTerrain, numtel, nomPack);

                abonnements.add(abonnement);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception de manière appropriée, la journaliser ou lancer une exception personnalisée
        }
        return abonnements;
    }

 */




    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM abonnement WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            // Set the parameter
            preparedStatement.setInt(1, id);

            // Execute the statement
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Abonnement supprimé");
            } else {
                System.out.println("Aucun abonnement trouvé avec l'ID spécifié");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTerrainIdByNom(String nomterrain) throws SQLException {
        String req = "SELECT id FROM terrain WHERE nom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, nomterrain);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                // Aucun terrain trouvé avec le nom spécifié
                throw new SQLException("Terrain introuvable avec le nom spécifié: " + nomterrain);
            }
        }
    }



    public int getPackIdByNom(String nomPack) throws SQLException {
        String req = "SELECT idP FROM pack WHERE nom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, nomPack);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("idP");
            } else {
                throw new SQLException("Pack introuvable avec le nom spécifié");
            }
        }
    }
    public List<Abonnement> getAllAbonnements() throws SQLException {
        List<Abonnement> abonnements = new ArrayList<>();

        // Requête SQL pour sélectionner tous les abonnements
        String sql = "SELECT * FROM abonnement";

        // Connexion à la base de données
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Parcourir les résultats de la requête
            while (resultSet.next()) {
                // Créer un objet Abonnement à partir des données de chaque ligne
                Abonnement abonnement = new Abonnement();
                abonnement.setId(resultSet.getInt("id"));
                abonnement.setDate(resultSet.getDate("date"));
                abonnement.setPrix(resultSet.getFloat("prix"));
                abonnement.setTerrainId(resultSet.getInt("terrainId"));
                abonnement.setNomPack(resultSet.getString("nomPack"));
                abonnement.setNomTerrain(resultSet.getString("nomterrain"));
                abonnement.setNomUser(resultSet.getString("nomUser"));
                abonnement.setNumtel(resultSet.getInt("numtel"));
                abonnement.setPrixTotal(resultSet.getFloat("prixTotal"));
                abonnement.setPackId(resultSet.getInt("packId"));


                // Ajouter l'abonnement à la liste
                abonnements.add(abonnement);
            }
        }

        return abonnements;
    }

    public List<Integer> getAllTerrainId() throws SQLException {
        List<Integer> terrainIds = new ArrayList<>();
        String req = "SELECT id FROM terrain";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idTerrain = resultSet.getInt("id");
                terrainIds.add(idTerrain);
            }
        }
        return terrainIds;
    }
    public void modifierAbonnement(Abonnement abonnement) throws SQLException {
        // Requête SQL pour mettre à jour l'abonnement avec l'ID spécifié
        String sql = "UPDATE abonnement SET date = ?, prix = ?, nomTerrain = ?, nomPack = ?, nomUser = ?, numtel = ?, prixTotal = ? WHERE id = ?";

        // Connexion à la base de données
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Convertir java.util.Date en java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(abonnement.getDate().getTime());

            // Définir les valeurs des paramètres de la requête
            statement.setDate(1, sqlDate);
            statement.setFloat(2, abonnement.getPrix());
            statement.setString(3, abonnement.getNomTerrain());
            statement.setString(4, abonnement.getNomPack());
            statement.setString(5, abonnement.getNomUser());
            statement.setInt(6, abonnement.getNumtel());
            statement.setFloat(7, abonnement.getPrixTotal());
            statement.setInt(8, abonnement.getId());

            // Exécuter la requête SQL
            int rowsAffected = statement.executeUpdate();

            // Vérifier si la mise à jour a été effectuée avec succès
            if (rowsAffected > 0) {
                System.out.println("Abonnement modifié avec succès");

                // Rafraîchir l'affichage des abonnements (si nécessaire)

            } else {
                System.out.println("Aucun abonnement trouvé avec l'ID spécifié");
            }
        }
    }

    public float getPrixTerrainPourPackPremium(int terrainId) throws SQLException {
        String req = "SELECT prix FROM terrain WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, terrainId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getFloat("prix");
            } else {
                throw new SQLException("Terrain introuvable avec l'ID spécifié: " + terrainId);
            }
        }
    }
    public boolean abonnementExistsWithSameName(String nomuser, Date date) {
        String req = "SELECT COUNT(*) FROM abonnement WHERE nomUser = ? AND  date = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, nomuser);
            // Convertir java.util.Date en java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            ps.setDate(2, sqlDate); // Utiliser setDate pour définir une date dans la requête préparée
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
