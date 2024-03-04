package services;

import entities.Pack;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePack {
    Connection connection;

    public ServicePack() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Pack pack) throws SQLException {
        String req = "INSERT INTO pack (nom, description, reduction, image) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            // Set parameters
            preparedStatement.setString(1, pack.getNom());
            preparedStatement.setString(2, pack.getDescription());
            preparedStatement.setFloat(3, pack.getReduction());
            preparedStatement.setString(4, pack.getImage());

            // Execute the statement
            preparedStatement.executeUpdate();
            System.out.println("Pack ajouté");
        }
    }

    public void modifier(Pack pack) throws SQLException {
        String req = "UPDATE pack SET nom=?, description=?, reduction=?, image=? WHERE idP=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, pack.getNom());
            preparedStatement.setString(2, pack.getDescription());
            preparedStatement.setFloat(3, pack.getReduction());
            preparedStatement.setString(4, pack.getImage());
            preparedStatement.setInt(5, pack.getIdP());

            preparedStatement.executeUpdate();
            System.out.println("Pack modifié");

            // Mettre à jour le nom du pack dans la table Abonnement
            mettreAJourNomPackDansAbonnements(pack.getIdP(), pack.getNom());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void mettreAJourNomPackDansAbonnements(int packId, String nouveauNomPack) throws SQLException {
        String req = "UPDATE abonnement SET nomPack=? WHERE packId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, nouveauNomPack);
            preparedStatement.setInt(2, packId);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " abonnements mis à jour avec le nouveau nom du pack");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }




    public void supprimer(int idP) throws SQLException {
        String req = "DELETE FROM pack WHERE idP=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            // Set the parameter
            preparedStatement.setInt(1, idP);

            // Execute the statement
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pack supprimé");
            } else {
                System.out.println("Aucun pack trouvé avec l'ID spécifié");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public List<Pack> afficherByName(String name) {
      List <Pack> packs=new ArrayList<>();
      String req="SELECT * FROM pack where nom=?";
      try{
          PreparedStatement ste=connection.prepareStatement(req);
          ste.setString(1,name);
          ResultSet rs= ste.executeQuery();
          while(rs.next())
          {
              int id=rs.getInt(1);
              String nom=rs.getString(2);
              String description=rs.getString(3);
              int red=rs.getInt(4);
              String im=rs.getString(5);
              Pack p=new Pack(nom,description,im,red);
            packs.add(p);

          }




      } catch (SQLException e) {
          throw new RuntimeException(e);
      }
      return packs;
    }

    public List<Pack> afficher() {
        List<Pack> packs = new ArrayList<>();
        String req = "SELECT * FROM pack";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(req)

        ) {

            while (resultSet.next()) {
                Pack pack = new Pack();
                pack.setIdP(resultSet.getInt("idP"));
                pack.setNom(resultSet.getString("nom"));
                pack.setDescription(resultSet.getString("description"));
                pack.setReduction(resultSet.getInt("reduction"));
                pack.setImage(resultSet.getString("image"));

                packs.add(pack);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packs;
    }

    public boolean packExistsWithSameName(String packName) {
        String req = "SELECT COUNT(*) FROM pack WHERE nom = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, packName);
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

    public List<Pack> afficherByNameStartingWith(String namePrefix) {
        List<Pack> packs = new ArrayList<>();
        String req = "SELECT * FROM pack WHERE nom LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, namePrefix + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("idP");
                String nom = resultSet.getString("nom");
                String description = resultSet.getString("description");
                int reduction = resultSet.getInt("reduction");
                String image = resultSet.getString("image");
                Pack pack = new Pack(id, nom, description, image ,reduction );
                packs.add(pack);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return packs;
    }
    public List<Pack> afficherByReduction(int reduction) {
        List<Pack> packs = new ArrayList<>();
        String req = "SELECT * FROM pack WHERE reduction = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setInt(1, reduction);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Pack pack = new Pack();
                pack.setIdP(resultSet.getInt("idP"));
                pack.setNom(resultSet.getString("nom"));
                pack.setDescription(resultSet.getString("description"));
                pack.setReduction(resultSet.getInt("reduction"));
                pack.setImage(resultSet.getString("image"));
                packs.add(pack);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packs;
    }
    public List<Pack> afficherBySearch(String searchText) {
        List<Pack> packs = new ArrayList<>();
        String req = "SELECT * FROM pack WHERE nom LIKE ? OR reduction = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, "%" + searchText + "%"); // Recherche par nom de pack
            if (isNumeric(searchText)) {
                statement.setInt(2, Integer.parseInt(searchText)); // Recherche par réduction
            } else {
                statement.setInt(2, -1); // Réduction fictive pour ignorer cette clause
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("idP");
                String nom = resultSet.getString("nom");
                String description = resultSet.getString("description");
                int reduction = resultSet.getInt("reduction");
                String image = resultSet.getString("image");
                Pack pack = new Pack(id, nom, description, image ,reduction);
                packs.add(pack);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return packs;
    }
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }


    }

    public List<Pack> afficherByPartialReduction(String partialReduction) {
        List<Pack> packs = new ArrayList<>();
        String req = "SELECT * FROM pack WHERE reduction LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, partialReduction + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("idP");
                String nom = resultSet.getString("nom");
                String description = resultSet.getString("description");
                int reduction = resultSet.getInt("reduction");
                String image = resultSet.getString("image");
                Pack pack = new Pack(id, nom, description, image, reduction);
                packs.add(pack);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return packs;
    }




}
