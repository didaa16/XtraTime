package controllers;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import utils.Encryptor;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private Label echecLoginLabel;

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
        if(nouveauMdp.getText().isBlank()|| nouveauMdp.getText().length() < 8 || nouveauMdp.getText().matches("[^a-zA-Z0-9]")){
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
    void initialize() {
    }

}
