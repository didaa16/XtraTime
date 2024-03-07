package controllers.utilisateur;

import entities.utilisateur.Img;
import entities.utilisateur.Utilisateur;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.utilisateur.ServiceImg;
import services.utilisateur.ServiceUtilisateurs;
import utils.Encryptor;
import utils.SendMail;
import utils.SendSMS;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class locateurFrontController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Menu;

    @FXML
    private Button MenuClose;

    @FXML
    private Label acienMdpErrorLabel;

    @FXML
    private TextField ageTF;

    @FXML
    private PasswordField ancienMdpTF;

    @FXML
    private Pane changementPane;

    @FXML
    private TextField cinTF;

    @FXML
    private TextField codeVerification;

    @FXML
    private ProgressBar complexiteBar;

    @FXML
    private Label complexiteLabel;

    @FXML
    private Button confirmerAdresseCode, confirmerAdresseCode1;

    @FXML
    private Label confirmerMdpErrorLabel;

    @FXML
    private PasswordField confirmerNouveauMdpTF;

    @FXML
    private Button deconnecterButton, voir, supprimer;

    @FXML
    private TextField emailTF;

    @FXML
    private Label errorCodeVerificationLabel;

    @FXML
    private Hyperlink mdpOublieHL;

    @FXML
    private Label messageLabel;

    @FXML
    private Button modifierProfile;

    @FXML
    private Button motDePasseButton;

    @FXML
    private TextField nomTF;

    @FXML
    private Button notificationsButton;

    @FXML
    private PasswordField nouveauMdpTF;

    @FXML
    private TextField numTelTF;

    @FXML
    private Pane parametresMdpPane;

    @FXML
    private ScrollPane politiqueConfidentialiteScroll;

    @FXML
    private Button politiqueDeConfidentialiteButton;

    @FXML
    private Text nomEtPrenom;

    @FXML
    private TextArea politiquesText;
    @FXML
    private AnchorPane anchorMain;

    @FXML
    private TextField prenomTF;

    @FXML
    private Button profileButton;

    @FXML
    private Pane profilePane;

    @FXML
    private TextField pseudoTF;

    @FXML
    private Button retourToMdp;

    @FXML
    private Button retourToProfile;

    @FXML
    private Button sauvegarderMdpChangement;

    @FXML
    private Button sauvegarderModificationsButton;



    @FXML
    private VBox vboxDown;
    @FXML
    private AnchorPane slider;

    @FXML
    private VBox vboxUp;
    @FXML
    private AnchorPane selectModeAnchor;
    private static boolean exist;
    private static Utilisateur loggedInUser;
    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }
    @FXML
    private Circle pdp, pdp1;
    private static String url;
    private static Img loggedInImage;
    private static int rand;
    private static void setRand(int r) {
        rand = r;
    }
    private static boolean deleted;
    ServiceUtilisateurs serviceUtilisateurs;
    ServiceImg serviceImg;
    Encryptor encryptor = new Encryptor();
    private boolean sliderVisible = false;

    @FXML
    void notificationsButtonOnClick(ActionEvent event) {

    }



    @FXML
    void deconnecterButtonOnClick(ActionEvent event) {
        loggedInUser = null;
        serviceUtilisateurs.changeScreen(event, "/FxmlUtilisateur/login.fxml", "LOGIN");
    }

    @FXML
    void mdpOublieHLOnClick(ActionEvent event) {
        if (loggedInUser!=null){
            parametresMdpPane.setVisible(false);
            selectModeAnchor.setVisible(true);
            anchorMain.getChildren().clear();
            anchorMain.setVisible(false);
        }
    }
    @FXML
    private void verifierEmailOnClick(ActionEvent event) throws SQLException {
        Random rd = new Random();
        int Rand = rd.nextInt(1000000 + 1);
        motDePasseOublie.setLoggedInUser(loggedInUser);
        motDePasseOublie.setRand(Rand);
        serviceUtilisateurs.changeScreen(event, "/FxmlUtilisateur/motDePasseOublie.fxml", "Vérifier le code");
        SendMail.SendMail(event, Rand, loggedInUser);
    }

    @FXML
    private void verifierNumTelOnClick(ActionEvent event){
        Random rd = new Random();
        int Ra = rd.nextInt(1000000+1);
        motDePasseOublie.setRand(Ra);
        String message = "Bonjour " + loggedInUser.getNom() + " Voici votre code de Confirmation de le mot de passe : " + String.valueOf(Ra);
        System.out.println(message);
        String num = "+216"+String.valueOf(loggedInUser.getNumtel());
        System.out.println(num);
        SendSMS.SendSMS(message, num);
        motDePasseOublie.setLoggedInUser(loggedInUser);
        serviceUtilisateurs.changeScreen(event, "/FxmlUtilisateur/motDePasseOublie.fxml", "Vérifier le code");

    }
    @FXML
    private void retourButtonOnClick(ActionEvent event){
        parametresMdpPane.setVisible(true);
        selectModeAnchor.setVisible(false);
        anchorMain.getChildren().clear();
        anchorMain.setVisible(false);
    }

    @FXML
    void motDePasseButtonOnClick(ActionEvent event) {
        changementPane.setVisible(false);
        parametresMdpPane.setVisible(true);
        politiqueConfidentialiteScroll.setVisible(false);
        profilePane.setVisible(false);
        anchorMain.getChildren().clear();
        anchorMain.setVisible(false);
    }

    @FXML
    void politiqueDeConfidentialiteButtonOnClick(ActionEvent event) {
        changementPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        politiqueConfidentialiteScroll.setVisible(true);
        profilePane.setVisible(false);
        anchorMain.getChildren().clear();
        anchorMain.setVisible(false);
        politiquesText.setText("Politique de confidentialité de l'application XtraTime\n" +
                "\n" +
                "Dernière mise à jour : [date de la dernière mise à jour]\n" +
                "\n" +
                "La présente Politique de confidentialité a pour but de vous informer de la manière dont XtraTime collecte, utilise, protège et partage vos informations lorsque vous utilisez notre application XtraTime. Votre vie privée est très importante pour nous, c'est pourquoi nous nous engageons à protéger vos données personnelles et à les traiter de manière transparente et sécurisée.\n" +
                "\n" +
                "Informations que nous collectons :\n" +
                "\n" +
                "a. Informations d'identification personnelle : Lorsque vous créez un compte sur XtraTime, nous pouvons collecter des informations telles que votre nom, votre adresse e-mail, votre numéro de téléphone, votre adresse postale et d'autres informations similaires.\n" +
                "\n" +
                "b. Informations de paiement : Si vous effectuez des transactions à travers notre application, nous pouvons collecter des informations de paiement telles que les détails de votre carte de crédit ou de votre compte bancaire.\n" +
                "\n" +
                "c. Informations de connexion : Nous pouvons collecter des informations sur votre utilisation de notre application, y compris les pages que vous consultez, les fonctionnalités que vous utilisez et d'autres données similaires.\n" +
                "\n" +
                "d. Informations de localisation : Avec votre consentement, nous pouvons collecter des informations sur votre emplacement géographique afin de vous fournir des fonctionnalités basées sur la localisation, comme la recherche d'espaces sportifs à proximité.\n" +
                "\n" +
                "Utilisation des informations :\n" +
                "\n" +
                "a. Nous utilisons les informations collectées pour fournir, maintenir et améliorer notre application XtraTime, ainsi que pour personnaliser votre expérience utilisateur et vous fournir un service clientèle de qualité.\n" +
                "\n" +
                "b. Nous pouvons également utiliser vos informations pour vous envoyer des communications marketing, des newsletters ou d'autres informations sur nos produits et services, sous réserve de votre consentement préalable.\n" +
                "\n" +
                "c. Nous ne partagerons pas vos informations personnelles avec des tiers à des fins de marketing direct sans votre consentement explicite.\n" +
                "\n" +
                "d. Nous pouvons partager vos informations avec des prestataires de services tiers qui nous aident à fournir et à améliorer notre application, à traiter des paiements, à effectuer des analyses de données, à héberger des données, à fournir un support client, et à d'autres fins similaires. Ces prestataires de services sont tenus de protéger vos informations et de les utiliser uniquement conformément à nos instructions.\n" +
                "\n" +
                "Protection des informations :\n" +
                "\n" +
                "a. Nous prenons des mesures de sécurité appropriées pour protéger vos informations personnelles contre la perte, le vol, l'accès non autorisé, la divulgation, la modification ou la destruction.\n" +
                "\n" +
                "b. Nous limitons l'accès à vos informations personnelles aux employés qui ont besoin d'y accéder pour fournir des services ou des fonctionnalités de notre application.\n" +
                "\n" +
                "c. Nous effectuons régulièrement des audits de sécurité pour identifier et remédier aux vulnérabilités potentielles de notre système.\n" +
                "\n" +
                "Conservation des données :\n" +
                "\n" +
                "a. Nous conservons vos informations personnelles aussi longtemps que nécessaire pour remplir les objectifs pour lesquels elles ont été collectées, sauf si la loi nous oblige à les conserver plus longtemps.\n" +
                "\n" +
                "b. Vous pouvez demander la suppression de vos informations personnelles à tout moment en nous contactant à l'adresse indiquée ci-dessous.\n" +
                "\n" +
                "Vos droits :\n" +
                "\n" +
                "a. Vous avez le droit d'accéder à vos informations personnelles que nous détenons et de demander des informations sur la manière dont elles sont traitées.\n" +
                "\n" +
                "b. Vous avez le droit de rectifier, mettre à jour ou supprimer vos informations personnelles inexactes ou incomplètes.\n" +
                "\n" +
                "c. Vous avez le droit de limiter ou de vous opposer au traitement de vos informations personnelles dans certaines circonstances.\n" +
                "\n" +
                "Modifications de la politique de confidentialité :\n" +
                "\n" +
                "a. Nous pouvons mettre à jour cette Politique de confidentialité de temps à autre pour refléter les changements dans nos pratiques en matière de confidentialité. Nous vous recommandons de consulter régulièrement cette page pour rester informé des mises à jour.\n" +
                "\n" +
                "Contact :\n" +
                "\n" +
                "Si vous avez des questions ou des préoccupations concernant notre utilisation de vos informations personnelles ou si vous souhaitez exercer vos droits en matière de confidentialité, veuillez nous contacter à l'adresse suivante : [adresse e-mail de contact].\n" +
                "\n" +
                "Merci d'utiliser XtraTime !");
    }

    private boolean getErrorsMdp() throws NoSuchAlgorithmException {
        String thisUserMdp = loggedInUser.getMdp();
        String mdpSaisi = encryptor.encryptString(ancienMdpTF.getText());
        if(!Objects.equals(thisUserMdp, mdpSaisi)){
            acienMdpErrorLabel.setTextFill(Color.RED);
            acienMdpErrorLabel.setText("Ancien Mot de passe invalide");
            return true;
        }
        if(confirmerNouveauMdpTF.getText().isBlank()){
            confirmerMdpErrorLabel.setTextFill(Color.RED);
            confirmerMdpErrorLabel.setText("La confirmation du mot de passe est invalide");
            return true;
        }
        if(!Objects.equals(confirmerNouveauMdpTF.getText(), nouveauMdpTF.getText())){
            confirmerMdpErrorLabel.setTextFill(Color.RED);
            confirmerMdpErrorLabel.setText("Le mot de passe doit etre le meme");
            return true;
        }
        return false;
    }
    @FXML
    void sauvegarderMdpChangementOnClick(ActionEvent event) throws NoSuchAlgorithmException {
        if (!getErrorsMdp()){
            try {
                serviceUtilisateurs.modifierMdp(loggedInUser, encryptor.encryptString(nouveauMdpTF.getText()));
                JOptionPane.showMessageDialog(null,"Mot de Passe modifié avec succès !");
                parametresMdpPane.setVisible(false);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @FXML
    private void getComplexite() {
        nouveauMdpTF.textProperty().addListener((observable, oldValue, newValue) -> {
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
    void modifierProfileOnClick(ActionEvent event) {
        changementPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        politiqueConfidentialiteScroll.setVisible(false);
        profilePane.setVisible(true);
        sauvegarderModificationsButton.setVisible(true);
        anchorMain.getChildren().clear();
        anchorMain.setVisible(false);
    }


    public boolean getErrors(){
        if(pseudoTF.getText().isBlank()){
            JOptionPane.showMessageDialog(null, "Le Pseudo est invalid", "Erreur", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        if(cinTF.getText().isBlank() || !cinTF.getText().matches("\\d{8}")){
            JOptionPane.showMessageDialog(null, "Le CIN est invalide", "Erreur", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        if(nomTF.getText().isBlank() || !nomTF.getText().matches("[a-zA-Z ]+")){
            JOptionPane.showMessageDialog(null, "Le nom est invalide", "Erreur", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        if(prenomTF.getText().isBlank() || !prenomTF.getText().matches("[a-zA-Z ]+")){
            JOptionPane.showMessageDialog(null, "Le prénom est invalide", "Erreur", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        if(ageTF.getText().isBlank() || !ageTF.getText().matches("\\d+") || Integer.parseInt(ageTF.getText()) < 18){
            JOptionPane.showMessageDialog(null, "L'âge doit être un nombre valide et être supérieur à 18", "Erreur", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        if(numTelTF.getText().isBlank() || !numTelTF.getText().matches("\\d{1,12}")){
            JOptionPane.showMessageDialog(null, "Le numéro de téléphone est invalide", "Erreur", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        if(emailTF.getText().isBlank() || !emailTF.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            JOptionPane.showMessageDialog(null, "L'email est invalide", "Erreur", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    @FXML
    void sauvegarderModificationsButtonOnClick(ActionEvent event) {
        if (!getErrors()) {
            try {
                String oldEmail = loggedInUser.getEmail();
                Utilisateur newUser = new Utilisateur(loggedInUser.getPseudo(), Integer.parseInt(cinTF.getText()), nomTF.getText(),
                        prenomTF.getText(), Integer.parseInt(ageTF.getText()), Integer.parseInt(numTelTF.getText()),
                        emailTF.getText(), loggedInUser.getMdp(), loggedInUser.getRole());

                if (!oldEmail.equals(emailTF.getText())) {
                    Random rd = new Random();
                    int Ra = rd.nextInt(1000000+1);
                    setRand(Ra);
                    SendMail.SendMail(event, Ra, loggedInUser);
                    changementPane.setVisible(true);
                    profilePane.setVisible(false);
                    messageLabel.setTextFill(Color.WHITE);
                    messageLabel.setText("Vous avez modifié votre adresse e-mail c'est pour cela vous avez reçu un code de vérification dans votre ancien email pour vérifier votre identité!");
                    confirmerAdresseCode1.setVisible(false);
                    confirmerAdresseCode.setVisible(true);
                } else {
                    // Vérifie si l'utilisateur existe déjà dans la base de données
                    if (serviceUtilisateurs.pseudoExiste(newUser.getPseudo())) {
                        // Si l'utilisateur existe, appeler la méthode modifier
                        serviceUtilisateurs.modifier(newUser);
                        Path source = Paths.get(url); // Assuming url is the path to the image
                        Path destination = Paths.get("C:\\Users\\PC\\OneDrive\\Bureau\\STUDY\\SEMESTRE 2\\PI\\XtraTime\\src\\main\\resources\\uploads\\" + newUser.getPseudo() + ".jpg");
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        Img img = new Img(loggedInUser.getPseudo(), destination.toString());
                        System.out.println(img);
                        System.out.println(img);
                        if (serviceImg.imgExiste(img.getPseudoU())) {
                            if (deleted) {
                                serviceImg.supprimer(img);
                            } else {
                                serviceImg.modifier(img);
                            }
                        } else {
                            serviceImg.ajouter(img);
                        }
                        setData();
                        JOptionPane.showMessageDialog(null,"Modification effectuée! ");
                        System.out.println("Utilisateur modifié avec succès !");
                    } else {
                        // Si l'utilisateur n'existe pas, appeler la méthode ajouter
                        newUser.setRole("Client");
                        Random rd = new Random();
                        int Rdd = rd.nextInt(1000000+1);
                        String newMdp = encryptor.encryptString(String.valueOf(Rdd));
                        newUser.setMdp(newMdp);
                        serviceUtilisateurs.ajouter(newUser);
                        ancienMdpTF.setText(String.valueOf(Rdd));
                        loggedInUser = newUser;
                        JOptionPane.showMessageDialog(null,"Inscription avec succés! Bienvenue à XtraTime \n Voici votre nouveau mot de passe : "+Rdd+" \n vous pouvez le modifier quand tu veux.");
                        Path source = Paths.get(url); // Assuming url is the path to the image
                        Path destination = Paths.get("C:\\Users\\PC\\OneDrive\\Bureau\\STUDY\\SEMESTRE 2\\PI\\XtraTime\\src\\main\\resources\\uploads\\" + newUser.getPseudo() + ".jpg");
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        Img img = new Img(newUser.getPseudo(), destination.toString());
                        System.out.println(img);
                        if (serviceImg.imgExiste(img.getPseudoU())) {
                            if (deleted) {
                                serviceImg.supprimer(img);
                            } else {
                                serviceImg.modifier(img);
                            }
                        } else {
                            serviceImg.ajouter(img);
                        }
                        setData();
                        JOptionPane.showMessageDialog(null,"Utilisateur ajouté! ");
                        System.out.println("Utilisateur ajouté avec succès !");
                    }
                }
            } catch (SQLException | NoSuchAlgorithmException | IOException e) {
                System.out.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
            }
        }
    }

    @FXML
    void retourToProfileOnClick(ActionEvent event) {
        changementPane.setVisible(false);
        profilePane.setVisible(true);
        anchorMain.getChildren().clear();
        anchorMain.setVisible(false);
    }
    @FXML
    void confirmerAdresseCodeOnClick(ActionEvent event) {
        String codeTexte = codeVerification.getText().trim(); // Supprimer les espaces blancs au début et à la fin de la chaîne
        try {
            int code = Integer.parseInt(codeTexte);
            if (code == rand) {
                Utilisateur newUser = new Utilisateur(loggedInUser.getPseudo(), Integer.parseInt(cinTF.getText()), nomTF.getText(),
                        prenomTF.getText(), Integer.parseInt(ageTF.getText()), Integer.parseInt(numTelTF.getText()),
                        emailTF.getText(), loggedInUser.getMdp(), loggedInUser.getRole());
                messageLabel.setTextFill(Color.WHITE);
                messageLabel.setText("Merci de votre coopération, maintenant vous avez reçu un autre code de vérification dans votre nouvelle adresse e-mail pour vérifier votre identité!");
                confirmerAdresseCode1.setVisible(true);
                confirmerAdresseCode.setVisible(false);
                codeVerification.clear();
                Random rd = new Random();
                int Ra = rd.nextInt(1000000+1);
                setRand(Ra);
                SendMail.SendMail(event, Ra, newUser);
            } else {
                JOptionPane.showMessageDialog(null,"Code incorrect ! ");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"Code incorrect ! ");
            e.printStackTrace(); // Afficher la trace de la pile pour le débogage
        }
    }

    @FXML
    void confirmerAdresseCodeOnClick1(ActionEvent event) {
        if (Integer.parseInt(codeVerification.getText()) == rand){
            Utilisateur newUser = new Utilisateur(loggedInUser.getPseudo(), Integer.parseInt(cinTF.getText()), nomTF.getText(),
                    prenomTF.getText(), Integer.parseInt(ageTF.getText()), Integer.parseInt(numTelTF.getText()),
                    emailTF.getText(), loggedInUser.getMdp(), loggedInUser.getRole());
            try {
                serviceUtilisateurs.modifier(newUser);
                Path source = Paths.get(url); // Assuming url is the path to the image
                Path destination = Paths.get("C:\\Users\\PC\\OneDrive\\Bureau\\STUDY\\SEMESTRE 2\\PI\\XtraTime\\src\\main\\resources\\uploads\\" + newUser.getPseudo() + ".jpg");
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                Img img = new Img(newUser.getPseudo(), destination.toString());
                System.out.println(img);
                if(serviceImg.imgExiste(img.getPseudoU())){
                    if (deleted){
                        serviceImg.supprimer(img);
                    }else {
                        serviceImg.modifier(img);
                    }
                }else {
                    serviceImg.ajouter(img);
                }
                setData();
                JOptionPane.showMessageDialog(null,"Modification effectuée! ");
                System.out.println("Utilisateur modifié avec succès !");
                changementPane.setVisible(false);
                profilePane.setVisible(true);
            } catch (SQLException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            JOptionPane.showMessageDialog(null,"Code incorrecte! ");
        }
    }

    private void setData() throws SQLException {
        serviceUtilisateurs = new ServiceUtilisateurs();
        serviceImg = new ServiceImg();
        exist = serviceImg.imgExiste(loggedInUser.getPseudo());
        deleted = false;
        if (loggedInUser!=null){
            loggedInImage = serviceImg.afficherImageParPseudo(loggedInUser.getPseudo());
            if (serviceUtilisateurs.pseudoExiste(loggedInUser.getPseudo())){
                Utilisateur user = serviceUtilisateurs.afficherParPseudo(loggedInUser.getPseudo());
                nomEtPrenom.setText(user.getNom() + " " + user.getPrenom());
                pseudoTF.setText(user.getPseudo());
                cinTF.setText(String.valueOf(user.getCin()));
                nomTF.setText(user.getNom());
                prenomTF.setText(user.getPrenom());
                ageTF.setText(String.valueOf(user.getAge()));
                numTelTF.setText(String.valueOf(user.getNumtel()));
                emailTF.setText(user.getEmail());
            }
            else {
                if (loggedInUser.getPrenom() == null && loggedInUser.getNom() != null) {
                    nomEtPrenom.setText(loggedInUser.getNom());
                } else if (loggedInUser.getPrenom() != null && loggedInUser.getNom() == null) {
                    nomEtPrenom.setText(loggedInUser.getPrenom());
                } else if (loggedInUser.getPrenom() == null && loggedInUser.getNom() == null) {
                    nomEtPrenom.setText("XtraTime");
                } else {
                    nomEtPrenom.setText(loggedInUser.getNom() + " " + loggedInUser.getPrenom());
                }
                pseudoTF.setText(loggedInUser.getPseudo());
                cinTF.setText(String.valueOf(loggedInUser.getCin()));
                nomTF.setText(loggedInUser.getNom());
                prenomTF.setText(loggedInUser.getPrenom());
                ageTF.setText(String.valueOf(loggedInUser.getAge()));
                numTelTF.setText(String.valueOf(loggedInUser.getNumtel()));
                emailTF.setText(loggedInUser.getEmail());
            }
            if (exist) {
                String absolutePath = loggedInImage.getImg();
                Image image = new Image(new File(absolutePath).toURI().toString());
                pdp.setFill(new ImagePattern(image));
                pdp1.setFill(new ImagePattern(image));
            } else {
                String absolutePath = "/Design/pdpVide.jpg";
                Image image = new Image(getClass().getResourceAsStream(absolutePath));
                pdp.setFill(new ImagePattern(image));
                pdp1.setFill(new ImagePattern(image));
            }
        }
    }

    @FXML
    void initialize() throws SQLException {
        slider.setTranslateX(-200);

        // Ajoutez un écouteur d'événements pour profileButton
        profileButton.setOnMouseClicked(event -> {
            if (!sliderVisible) {
                profilePane.setVisible(true);
                sauvegarderModificationsButton.setVisible(false);
                anchorMain.getChildren().clear();
                anchorMain.setVisible(false);
                // Si le slider n'est pas déjà visible, l'afficher
                TranslateTransition slide = new TranslateTransition();
                slide.setDuration(Duration.seconds(0.4));
                slide.setNode(slider);
                slide.setToX(0);
                slide.play();
                sliderVisible = true;
            } else {
                // Sinon, le cacher
                TranslateTransition slide = new TranslateTransition();
                slide.setDuration(Duration.seconds(0.4));
                slide.setNode(slider);
                slide.setToX(-200);
                slide.play();
                sliderVisible = false;
            }
        });
        pseudoTF.setStyle("-fx-text-fill: white;");
        cinTF.setStyle("-fx-text-fill: white;");
        nomTF.setStyle("-fx-text-fill: white;");
        prenomTF.setStyle("-fx-text-fill: white;");
        numTelTF.setStyle("-fx-text-fill: white;");
        emailTF.setStyle("-fx-text-fill: white;");
        ageTF.setStyle("-fx-text-fill: white;");
        ancienMdpTF.setStyle("-fx-text-fill: white;");
        confirmerNouveauMdpTF.setStyle("-fx-text-fill: white;");
        nouveauMdpTF.setStyle("-fx-text-fill: white;");
        codeVerification.setStyle("-fx-text-fill: white;");
        setData();
        getComplexite();
    }

    public void importerOnClick(ActionEvent event) {
        FileChooser fileChooser1 = new FileChooser();
        fileChooser1.setTitle("Open Image File");
        File file = fileChooser1.showOpenDialog(null);
        if (file != null) {
            String absolutePath = file.getAbsolutePath();
            url=absolutePath;
            javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
            pdp.setFill(new ImagePattern(image));
        }
    }

    public void supprimerPdp(ActionEvent event) {
        pdp.setFill(new ImagePattern(new Image("/Design/pdpVide.jpg")));
        deleted = true;
    }
    public void voirPdp(ActionEvent event) throws SQLException {
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        ImageView imageView = new ImageView();
        if (serviceImg.imgExiste(loggedInUser.getPseudo())) {
            String absolutePath = loggedInImage.getImg();
            Image image = new Image(new File(absolutePath).toURI().toString());
            imageView.setImage(image);
        } else {
            String absolutePath = "/Design/pdpVide.jpg";
            Image image = new Image(getClass().getResourceAsStream(absolutePath));
            imageView.setImage(image);
        }
        imageView.setFitWidth(650); // Set desired width
        imageView.setFitHeight(650);
        imageView.setPreserveRatio(true); // Preserve aspect ratio
        pane.getChildren().add(imageView);

        stage.setScene(scene);
        stage.show();
    }

    public void redirection(String loader) throws IOException {
        changementPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        selectModeAnchor.setVisible(false);
        profilePane.setVisible(false);
        anchorMain.setVisible(true);
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource(loader));
        anchorMain.getChildren().add(root);
    }
    @FXML
    private void complexButtonOnClick(ActionEvent event) throws IOException {
        changementPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        selectModeAnchor.setVisible(false);
        profilePane.setVisible(false);
        anchorMain.setVisible(true);
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource("/FxmlLocal/AfficherComplexe.fxml"));
        anchorMain.getChildren().add(root);
    }
    @FXML
    private void ajouterComplexe(ActionEvent event) throws IOException {
        changementPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        selectModeAnchor.setVisible(false);
        profilePane.setVisible(false);
        anchorMain.setVisible(true);
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource("/FxmlLocal/inscription.fxml"));
        anchorMain.getChildren().add(root);
    }
    @FXML
    private void afficherTerrain(ActionEvent event) throws IOException {
        changementPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        selectModeAnchor.setVisible(false);
        profilePane.setVisible(false);
        anchorMain.setVisible(true);
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource("/FxmlLocal/AffTerrain.fxml"));
        anchorMain.getChildren().add(root);
    }
    @FXML
    private void ajouterTerrain(ActionEvent event) throws IOException {
        changementPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        parametresMdpPane.setVisible(false);
        selectModeAnchor.setVisible(false);
        profilePane.setVisible(false);
        anchorMain.setVisible(true);
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource("/FxmlLocal/AddTerrain.fxml"));
        anchorMain.getChildren().add(root);
    }

}