package controllers;

import com.restfb.*;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.json.JsonObject;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.ServiceImg;
import services.ServiceUtilisateurs;
import utils.SendMail;
import utils.SendSMS;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;

;

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



    @FXML
    private Circle circle;
    private static Utilisateur loggedInUser;

    private static int rand;
    private static void setRand(int r) {
        rand = r;
    }

    private String app_Id = "369482289342418";
    private String app_Secret = "17671719e3b5ec51f557f3c4e880a9e9";
    private String redirect_Url = "http://localhost:8181/";
    private String redirect_url_encoded = "http%3A%2F%2Flocalhost%3A8181%2F";
    private String state = "9812" ;
    private String code = "";
    private String authentication = "https://www.facebook.com/v19.0/dialog/oauth?client_id="+app_Id+"&redirect_uri="+redirect_url_encoded+"&state="+state;

    @FXML
    private void connectWithFacebook(ActionEvent event) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(authentication);

        Pane webViewPane = new Pane();
        webViewPane.getChildren().add(webView);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(webViewPane));
        stage.show();

        webEngine.locationProperty().addListener((obs, oldLocation, newLocation) -> {
            if (newLocation != null && newLocation.startsWith("http://localhost")) {
                URI uri = URI.create(newLocation);
                String code = null;
                for (String queryParam : uri.getQuery().split("&")) {
                    String[] keyValue = queryParam.split("=");
                    if (keyValue.length == 2 && keyValue[0].equals("code")) {
                        code = keyValue[1];
                        break;
                    }
                }
                if (code != null) {
                    try {
                        DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.VERSION_19_0);
                        AccessToken accessToken = facebookClient.obtainUserAccessToken(
                                app_Id, app_Secret, redirect_Url, code);

                        String access_token = accessToken.getAccessToken();

                        FacebookClient fbClient = new DefaultFacebookClient(access_token, Version.VERSION_19_0);

                        // Récupération des informations du profil de l'utilisateur
                        JsonObject userProfileJson = fbClient.fetchObject("me", JsonObject.class, Parameter.with("fields", "id,name,email,picture"));
                        String userId = userProfileJson.getString("id", String.valueOf(String.class));
                        String userName = userProfileJson.getString("name", String.valueOf(String.class));
                        String userEmail = userProfileJson.getString("email", String.valueOf(String.class));

                        // Récupération de l'URL de l'image de profil de l'utilisateur
                        String profilePicUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";

                        // Définition du chemin d'enregistrement de l'image
                        String folderPath = "C:\\Users\\PC\\OneDrive\\Bureau\\STUDY\\SEMESTRE 2\\PI\\XtraTime\\src\\main\\resources\\uploads";
                        String imagePath = folderPath + "\\" + userId + "_profile_pic.jpg";

                        // Téléchargement et enregistrement de l'image
                        URL url = new URL(profilePicUrl);
                        URLConnection connection = url.openConnection();
                        InputStream inputStream = connection.getInputStream();
                        FileOutputStream outputStream = new FileOutputStream(imagePath);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        inputStream.close();
                        outputStream.close();

                        // Création de l'objet Utilisateur et définition des attributs
                        Utilisateur user = new Utilisateur();
                        user.setPseudo(userId);
                        user.setNom(userName);
                        user.setEmail(userEmail);
                        clientFrontController.setLoggedInUser(user);

                        // Fermeture de la fenêtre de connexion et changement d'écran
                        JOptionPane.showMessageDialog(null,"Vous avez connecté avec succès! Si vous souhaitez garder votre compte persistant, allez à modifier et mettez vos autres informations.");
                        stage.close();
                        serviceUtilisateurs.changeScreen(event, "/clientFront.fxml", "XTRATIME");

                    } catch (FacebookOAuthException | IOException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Invalid redirect URI or code not found in the URL");
                }
            }
        });
    }





    @FXML
    void initialize() {
        circle = new Circle();
    }


    ServiceUtilisateurs serviceUtilisateurs = new ServiceUtilisateurs();
    ServiceImg serviceImg = new ServiceImg();

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
                        serviceUtilisateurs.changeScreen(event, "/dashboard.fxml", "XTRATIME");
                        break;
                    case "Client":
                        clientFrontController.setLoggedInUser(utilisateur);
                        serviceUtilisateurs.changeScreen(event, "/clientFront.fxml", "xtratime");
                        break;
                    case "Locateur":
                        locateurFrontController.setLoggedInUser(utilisateur);
                        serviceUtilisateurs.changeScreen(event, "/locateurFront.fxml", "Locateur");
                        break;
                    case "Livreur":
                        livreurFrontController.setLoggedInUser(utilisateur);
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
    @FXML
    private void verifierEmailOnClick(ActionEvent event) throws SQLException {
        Random rd = new Random();
        int Rand = rd.nextInt(1000000 + 1);
        Utilisateur newUser = serviceUtilisateurs.afficherParPseudo(pseudoLogin.getText());
        motDePasseOublie.setLoggedInUser(newUser);
        motDePasseOublie.setRand(Rand);
        serviceUtilisateurs.changeScreen(event, "/motDePasseOublie.fxml", "Vérifier le code");
        SendMail.SendMail(event, Rand, newUser);
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
            motDePasseOublie.setLoggedInUser(serviceUtilisateurs.afficherParPseudo(newUser.getPseudo()));
            serviceUtilisateurs.changeScreen(event, "/motDePasseOublie.fxml", "Vérifier le code");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    @FXML
    private void retourButtonOnClick(ActionEvent event){
        anchorLogin.setVisible(true);
        selectModeAnchor.setVisible(false);
    }



}
