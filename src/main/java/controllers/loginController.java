package controllers;
import com.google.api.client.http.javanet.NetHttpTransport;
import controllers.motDePasseOublie;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import services.ServiceUtilisateurs;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import utils.SendSMS;

public class loginController {
    @FXML
    private Label echecLoginLabel;
    @FXML
    private TextField pseudoLogin, mdpTextLogin;
    @FXML
    private PasswordField mdpLogin;
    @FXML
    private Button loginButton, signupLogin, eyeIconLogin, mdpOublie, verifierEmail, verifierNumTel;
    @FXML
    private AnchorPane selectModeAnchor, anchorLogin;


    private static int rand;
    private static void setRand(int r) {
        rand = r;
    }
    @FXML
    void initialize() {
    }


    ServiceUtilisateurs serviceUtilisateurs = new ServiceUtilisateurs();

    private void handleLogin(ActionEvent event) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        try {
            if (!serviceUtilisateurs.pseudoExiste(pseudoLogin.getText())) {
                echecLoginLabel.setTextFill(Color.RED);
                echecLoginLabel.setText("Pseudo ou mot de passe incorrect");
            }else if (!serviceUtilisateurs.utilisateurLoggedIn(pseudoLogin.getText(), mdpLogin.getText())){
                echecLoginLabel.setTextFill(Color.RED);
                echecLoginLabel.setText("Pseudo ou mot de passe incorrect");
                mdpOublie.setVisible(true);
            } else {
                Utilisateur utilisateur = serviceUtilisateurs.afficherParPseudo(pseudoLogin.getText());
                echecLoginLabel.setTextFill(Color.GREEN);
                echecLoginLabel.setText("Bienvenue, " + utilisateur.getNom());

                switch (utilisateur.getRole()) {
                    case "Admin":
                        dashboard.setLoggedInUser(utilisateur);
                        serviceUtilisateurs.changeScreen(event, "/dashboard.fxml", "Client");
                        break;
                    case "Client":
                        serviceUtilisateurs.changeScreen(event, "/clientFront.fxml", "Client");
                        break;
                    case "Locateur":
                        serviceUtilisateurs.changeScreen(event, "/locateurFront.fxml", "Locateur");
                        break;
                    case "Livreur":
                        serviceUtilisateurs.changeScreen(event, "/livreurFront.fxml", "Livreur");
                        break;
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion: " + e.getMessage());
        }
    }

    public void loginButtonOnClick(ActionEvent event) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (!mdpLogin.isVisible()){
            if (!pseudoLogin.getText().isBlank() && !mdpTextLogin.getText().isBlank()) {
                mdpLogin.setText(mdpTextLogin.getText());
                handleLogin(event);
            } else {
                echecLoginLabel.setTextFill(Color.RED);
                echecLoginLabel.setText("Entrer correctement votre pseudo et mot de passe");
            }
        }
        else {
            if (!pseudoLogin.getText().isBlank()&& !mdpLogin.getText().isBlank()){
                handleLogin(event);
            }
            else {
                echecLoginLabel.setTextFill(Color.RED);
                echecLoginLabel.setText("Entrer correctement votre pseudo et mot de passe");
            }

        }

    }

    public void signupLoginButtonOnClick(ActionEvent event){
        serviceUtilisateurs.changeScreen(event, "/signup.fxml", "Sign Up");
    }
    @FXML
    void eyeIconLoginButtonOnClick(ActionEvent event) {
        // Vérifier si le mot de passe est actuellement visible ou non
        boolean isPasswordVisible = mdpLogin.isVisible();
        mdpLogin.setVisible(!isPasswordVisible);

        // Changer l'image du bouton en conséquence
        if (isPasswordVisible) {
            mdpTextLogin.setText(mdpLogin.getText());
            mdpTextLogin.setVisible(true);
            eyeIconLogin.setStyle("-fx-background-image : url('../../resources/Design/eyeIcon1.png')");
        } else {
            mdpLogin.setText(mdpTextLogin.getText());
            mdpTextLogin.setVisible(false);
        }
    }


    @FXML
    private void mdpOublieOnClick(ActionEvent event){
        if (pseudoLogin.getText().isBlank()){
            echecLoginLabel.setTextFill(Color.TOMATO);
            echecLoginLabel.setText("Entrer le pseudo s'il vous plait !");
        }else {
            anchorLogin.setVisible(false);
            selectModeAnchor.setVisible(true);
        }
    }
    private void sendMail(ActionEvent event, int Rand){

        Properties props=new Properties();
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port",465);
        props.put("mail.smtp.user","bchirben8@gmail.com");
        props.put("mail.smtp.auth",true);
        props.put("mail.smtp.starttls.enable",true);
        props.put("mail.smtp.debug",true);
        props.put("mail.smtp.socketFactory.port",465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback",false);

        try {
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            message.setSubject("Code de Confirmation d'oublie le mot de passe");
            message.setFrom(new InternetAddress("bchirben8@gmail.com"));
            Utilisateur newUser = serviceUtilisateurs.afficherParPseudo(pseudoLogin.getText());
            motDePasseOublie.setLoggedInUser(newUser);
            message.setText("Bonjour " + newUser.getNom() + "\n Voici code de Confirmation d'oublie le mot de passe : " + String.valueOf(Rand));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(newUser.getEmail()));
            try
            {
                motDePasseOublie.setRand(Rand);
                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com","bchirben8@gmail.com","oeopsajyvhamngzz");
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
            }catch(Exception e)
            {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void verifierNumTelOnClick(ActionEvent event){
        Random rd = new Random();
        int Ra = rd.nextInt(1000000+1);
        motDePasseOublie.setRand(Ra);
        try {
            Utilisateur newUser = serviceUtilisateurs.afficherParPseudo(pseudoLogin.getText());
            motDePasseOublie.setLoggedInUser(newUser);
            String message = "Bonjour " + newUser.getNom() + " Voici votre code de Confirmation de le mot de passe : " + String.valueOf(Ra);
            System.out.println(message);
            String num = "+216"+String.valueOf(newUser.getNumtel());
            System.out.println(num);
            SendSMS.SendSMS(message, num);
            System.out.println("MOOOOOOOOOOOOOOo");
            motDePasseOublie.setLoggedInUser(serviceUtilisateurs.afficherParPseudo(newUser.getPseudo()));
            System.out.println("mochkla");
            serviceUtilisateurs.changeScreen(event, "/motDePasseOublie.fxml", "Vérifier le code");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    private void verifierEmailOnClick(ActionEvent event){
        Random rd = new Random();
        int Rand = rd.nextInt(1000000+1);
        serviceUtilisateurs.changeScreen(event, "/motDePasseOublie.fxml", "Vérifier le code");
        sendMail(event, Rand);
    }

    @FXML
    private void retourButtonOnClick(ActionEvent event){
        anchorLogin.setVisible(true);
        selectModeAnchor.setVisible(false);
    }



}
