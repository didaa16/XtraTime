package controllers;
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
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import services.ServiceUtilisateurs;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class loginController {
    @FXML
    private Button loginButton, signupLogin;
    @FXML
    private Label echecLoginLabel;
    @FXML
    private TextField pseudoLogin, mdpTextLogin;
    @FXML
    private PasswordField mdpLogin;
    @FXML
    private Button eyeIconLogin;
    @FXML
    private Button Exit;
    @FXML
    private Button mdpOublie;

    @FXML
    void initialize() {
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
    }


    ServiceUtilisateurs serviceUtilisateurs = new ServiceUtilisateurs();

    private void handleLogin(ActionEvent event) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        try {
            if (!serviceUtilisateurs.utilisateurLoggedIn(pseudoLogin.getText(), mdpLogin.getText())) {
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
            eyeIconLogin.setStyle("-fx-background-image: url('../../resources/Design/eyeIcon.png')");
        }
    }
    @FXML
    private void mdpOublieOnClick(ActionEvent event){
        Random rd = new Random();
        int Rand = rd.nextInt(1000000+1);
        sendMail(event, Rand);
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
            message.setText("Voici code de Confirmation d'oublie le mot de passe : " + String.valueOf(Rand));
            Utilisateur newUser = serviceUtilisateurs.afficherParPseudo(pseudoLogin.getText());
            motDePasseOublie.setLoggedInUser(newUser);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(newUser.getEmail()));
            try
            {
                motDePasseOublie.setRand(Rand);
                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com","bchirben8@gmail.com","oeopsajyvhamngzz");
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                serviceUtilisateurs.changeScreen(event, "/motDePasseOublie.fxml", "Vérifier le code");
            }catch(Exception e)
            {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }




}
