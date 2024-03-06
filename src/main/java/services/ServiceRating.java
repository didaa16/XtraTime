package services;

import utils.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceRating {


    public void addRating(String idUser, String ref, int rating) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();

        // Vérifiez que la connexion n'est pas null avant de l'utiliser
        if (connection == null) {
            throw new SQLException("La connexion à la base de données n'a pas été initialisée.");
        }

        String query = "INSERT INTO ratingProd (idUser, ref, rating) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idUser);
            statement.setString(2, ref);
            statement.setInt(3, rating);
            statement.executeUpdate();
        }
    }
    // Méthode pour ajouter un nouveau vote ou mettre à jour le vote existant
    public void addOrUpdateRating(String idUser, String ref, int rating) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();

        // Vérifiez que la connexion n'est pas null avant de l'utiliser
        if (connection == null) {
            throw new SQLException("La connexion à la base de données n'a pas été initialisée.");
        }

        // Vérifier si l'utilisateur a déjà voté pour ce produit
        boolean hasVoted = hasVoted(idUser, ref);

        if (hasVoted) {
            // Mettre à jour le vote existant
            updateRating(idUser, ref, rating);
        } else {
            // Ajouter un nouveau vote
            addRating(idUser, ref, rating);
        }
    }
    // Méthode pour mettre à jour un vote existant
    private void updateRating(String idUser, String ref, int rating) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();

        String query = "UPDATE ratingProd SET rating = ? WHERE idUser = ? AND ref = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, rating);
            statement.setString(2, idUser);
            statement.setString(3, ref);
            statement.executeUpdate();
        }
    }
    // Méthode pour vérifier si l'utilisateur a déjà voté pour un produit donné
    private boolean hasVoted(String idUser, String ref) throws SQLException {
        Connection connection = DataBase.getInstance().getConnection();
        boolean hasVoted = false;

        String query = "SELECT COUNT(*) FROM ratingProd WHERE idUser = ? AND ref = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idUser);
            statement.setString(2, ref);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                hasVoted = (count > 0);
            }
        }

        return hasVoted;
    }

    public double getAverageRating(String ref) throws SQLException {

        Connection connection = DataBase.getInstance().getConnection();
        double averageRating = 0;
        String query = "SELECT AVG(rating) FROM ratingProd WHERE ref = ?";
        PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, ref);
        ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    averageRating = rs.getDouble(1);

        }
        return averageRating;
    }


}
