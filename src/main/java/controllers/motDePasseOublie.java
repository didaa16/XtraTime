package controllers;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import utils.Encryptor;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import services.ServiceUtilisateurs;

import javax.swing.*;

public class motDePasseOublie {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Exit;

    @FXML
    private Label echecLoginLabel, complexiteLabel;

    @FXML
    private TextField codeVerification;

    @FXML
    private Button retourButton;

    @FXML
    private Button validerCode, validerMdp;
    @FXML
    private AnchorPane codeVerifAnchor, nouveauMdpAnchor;
    @FXML
    private PasswordField nouveauMdp, confirmerNouveauMdp;
    @FXML
    private ProgressBar complexiteBar;

    ServiceUtilisateurs serviceUtilisateurs = new ServiceUtilisateurs();
    Encryptor encryptor = new Encryptor();

    private static int rand;
    public static void setRand(int r) {
        rand = r;
    }
    private static Utilisateur loggedInUser;
    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }

    // Méthode getter pour obtenir le numéro aléatoire
    public static int getRand() {
        return rand;
    }
    @FXML
    void retourButtonOnClick(ActionEvent event) {
        serviceUtilisateurs.changeScreen(event, "/login.fxml", "LOGIN");
    }

    @FXML
    void validerCodeOnClick(ActionEvent event) {
        if (Integer.parseInt(codeVerification.getText()) == rand){
            codeVerifAnchor.setVisible(false);
            nouveauMdpAnchor.setVisible(true);
        }
        else {
            JOptionPane.showMessageDialog(null,"Code incorrecte! ");
        }
    }

    private boolean getError(){
        String thisUserMdp = loggedInUser.getMdp();
        if(nouveauMdp.getText().isBlank()|| complexiteLabel.getText().equals("Faible") || complexiteLabel.getText().equals("Très Faible")){
            echecLoginLabel.setTextFill(Color.RED);
            echecLoginLabel.setText("Le mot de passe est invalide");
            return true;
        }
        if(confirmerNouveauMdp.getText().isBlank()){
            echecLoginLabel.setTextFill(Color.RED);
            echecLoginLabel.setText("La confirmation du mot de passe est invalide");
            return true;
        }
        if(!Objects.equals(confirmerNouveauMdp.getText(), nouveauMdp.getText())){
            echecLoginLabel.setTextFill(Color.RED);
            echecLoginLabel.setText("Le mot de passe doit etre le meme");
            return true;
        }
        return false;
    }
    @FXML
    void validerMdpOnClick(ActionEvent event) throws NoSuchAlgorithmException {
        if (!getError()){
            try {
                serviceUtilisateurs.modifierMdp(loggedInUser, encryptor.encryptString(nouveauMdp.getText()));
                JOptionPane.showMessageDialog(null,"Mot de Passe modifié avec succès !");
                serviceUtilisateurs.changeScreen(event, "/login.fxml", "LOGIN");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void getComplexite() {
        nouveauMdp.textProperty().addListener((observable, oldValue, newValue) -> {
            complexiteBar.setVisible(true);

            // Reset complexity label and bar color
            complexiteLabel.setText("");
            complexiteBar.setProgress(0);
            complexiteBar.setBlendMode(null);

            // Check if the password contains special characters
            boolean hasSpecialChars = newValue.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
            // Check if the password contains digits
            boolean hasDigits = newValue.matches(".*\\d.*");
            // Check if the password contains lowercase letters
            boolean hasLowercase = newValue.matches(".*[a-z].*");
            // Check if the password contains uppercase letters
            boolean hasUppercase = newValue.matches(".*[A-Z].*");

            // Calculate complexity score
            int complexityScore = 0;
            if (hasSpecialChars) complexityScore += 2;
            if (hasDigits) complexityScore += 2;
            if (hasLowercase) complexityScore += 2;
            if (hasUppercase) complexityScore += 2;

            // Check if the password length is at least 8 characters
            if (newValue.length() < 8) {
                complexiteLabel.setText("Faible");
                complexiteBar.setProgress(0.0);
                complexiteBar.setBlendMode(BlendMode.RED);
            } else {
                // Set complexity level and progress bar based on score
                if (complexityScore >= 8) {
                    complexiteLabel.setText("Très Fort");
                    complexiteBar.setProgress(1.0);
                    complexiteBar.setBlendMode(BlendMode.COLOR_DODGE);
                } else if (complexityScore >= 6) {
                    complexiteLabel.setText("Fort");
                    complexiteBar.setProgress(0.75);
                    complexiteBar.setBlendMode(BlendMode.COLOR_DODGE);
                } else if (complexityScore >= 4) {
                    complexiteLabel.setText("Moyenne");
                    complexiteBar.setProgress(0.5);
                    complexiteBar.setBlendMode(BlendMode.HARD_LIGHT);
                } else if (complexityScore >= 2) {
                    complexiteLabel.setText("Faible");
                    complexiteBar.setProgress(0.25);
                    complexiteBar.setBlendMode(BlendMode.RED);
                } else {
                    complexiteLabel.setText("Très Faible");
                    complexiteBar.setProgress(0.0);
                    complexiteBar.setBlendMode(BlendMode.RED);
                }
            }
        });
    }

    @FXML
    void initialize() {
        getComplexite();
    }

}
