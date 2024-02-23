package controllers;
import entities.Utilisateur;
import utils.Encryptor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import services.ServiceUtilisateurs;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class signupController implements Initializable {
    ServiceUtilisateurs serviceUtilisateurs = new ServiceUtilisateurs();
    Encryptor encryptor = new Encryptor();

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    private TextField pseudoSignup, cinSignup, nomSignup, prenomSignup, ageSignup, numtelSignup, emailSignup, codeVerification;
    @FXML
    private PasswordField mdpSignup, confirmMdpSignup;
    @FXML
    private RadioButton roleClientSignup, roleLocateurSignup, roleLivreurSignup;
    @FXML
    private Button signUpButton, loginSignup, validerCode, retourButton;
    @FXML
    private Label pseudoSignupError, cinSignupError, nomSignupError, prenomSignupError, ageSignupError, numtelSignupError, emailSignupError, mdpSignupError, confirmMdpSignupError, roleSignupError ;
    @FXML
    private AnchorPane codeVerifAnchor, signUpAnchor ;


    private static int rand;
    private static void setRand(int r) {
        rand = r;
    }

    public void loginSignupButtonOnClick(ActionEvent event){
        serviceUtilisateurs.changeScreen(event, "/login.fxml", "Login");
    }

    public boolean getErrors(){
        pseudoSignupError.setText("");
        cinSignupError.setText("");
        nomSignupError.setText("");
        prenomSignupError.setText("");
        ageSignupError.setText("");
        numtelSignupError.setText("");
        emailSignupError.setText("");
        mdpSignupError.setText("");
        confirmMdpSignupError.setText("");
        roleSignupError.setText("");
        if(pseudoSignup.getText().isBlank()){
            pseudoSignupError.setTextFill(Color.RED);
            pseudoSignupError.setText("Le Pseudo est invalide");
            return true;
        }
        if(cinSignup.getText().isBlank() || !cinSignup.getText().matches("\\d{1,9}")){
            cinSignupError.setTextFill(Color.RED);
            cinSignupError.setText("Le CIN est invalide");
            return true;
        }
        if(nomSignup.getText().isBlank() || !nomSignup.getText().matches("[a-zA-Z ]+")){
            nomSignupError.setTextFill(Color.RED);
            nomSignupError.setText("Le nom est invalide");
            return true;
        }
        if(prenomSignup.getText().isBlank() || !prenomSignup.getText().matches("[a-zA-Z ]+")){
            prenomSignupError.setTextFill(Color.RED);
            prenomSignupError.setText("Le prénom est invalide");
            return true;
        }
        if(ageSignup.getText().isBlank() || !ageSignup.getText().matches("\\d+") || Integer.parseInt(ageSignup.getText()) < 18){
            ageSignupError.setTextFill(Color.RED);
            ageSignupError.setText("L'âge doit être un nombre valide et être supérieur à 18");
            return true;
        }
        if(numtelSignup.getText().isBlank() || !numtelSignup.getText().matches("\\d{1,12}")){
            numtelSignupError.setTextFill(Color.RED);
            numtelSignupError.setText("Le numéro de téléphone est invalide");
            return true;
        }
        if(emailSignup.getText().isBlank() || !emailSignup.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            emailSignupError.setTextFill(Color.RED);
            emailSignupError.setText("L'email est invalide");
            return true;
        }
        if(mdpSignup.getText().isBlank()|| mdpSignup.getText().length() < 8 || mdpSignup.getText().matches("[^a-zA-Z0-9]")){
            mdpSignupError.setTextFill(Color.RED);
            mdpSignupError.setText("Le mot de passe est invalide");
            return true;
        }
        if(confirmMdpSignup.getText().isBlank()){
            confirmMdpSignupError.setTextFill(Color.RED);
            confirmMdpSignupError.setText("La confirmation du mot de passe est invalide");
            return true;
        }
        if(!Objects.equals(confirmMdpSignup.getText(), mdpSignup.getText())){
            confirmMdpSignupError.setTextFill(Color.RED);
            confirmMdpSignupError.setText("Le mot de passe doit etre le meme");
            return true;
        }
        if(!roleClientSignup.isSelected() && !roleLocateurSignup.isSelected() && !roleLivreurSignup.isSelected()){
            roleSignupError.setTextFill(Color.RED);
            roleSignupError.setText("Le role est obligatoire");
            return true;
        }
        try {
            if (serviceUtilisateurs.pseudoExiste(pseudoSignup.getText())){
                pseudoSignupError.setTextFill(Color.RED);
                pseudoSignupError.setText("Ce pseudo est déjà utilisé, veuillez en choisir un autre");
                return true;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
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
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailSignup.getText()));
            try
            {
                signupController.setRand(Rand);
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
    public void signUpButtonButtonOnClick(ActionEvent event){
        if (!getErrors()) {
            Random rd = new Random();
            int Rand = rd.nextInt(1000000+1);
            signupController.setRand(Rand);
            signUpAnchor.setVisible(false);
            codeVerifAnchor.setVisible(true);
            sendMail(event, Rand);
        }
    }
    @FXML
    void validateCodeOnClick(ActionEvent event) throws NoSuchAlgorithmException {
        System.out.println(String.valueOf(rand));
        if (Integer.parseInt(codeVerification.getText()) == this.rand){
            Utilisateur newUser = new Utilisateur(pseudoSignup.getText(), Integer.parseInt(cinSignup.getText()), nomSignup.getText(),
                    prenomSignup.getText(), Integer.parseInt(ageSignup.getText()), Integer.parseInt(numtelSignup.getText()), emailSignup.getText(),
                    encryptor.encryptString(mdpSignup.getText()), (roleClientSignup.isSelected() ? "Client" : (roleLocateurSignup.isSelected() ? "Locateur" : "Livreur")));
            try {
                serviceUtilisateurs.ajouter(newUser);
                System.out.println("Utilisateur ajouté avec succès !");
                JOptionPane.showMessageDialog(null,"Vous etes inscris avec succès ! Veuillez connecter maintenant.");
                serviceUtilisateurs.changeScreen(event, "/login.fxml", "LOGIN");
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            }
        }
        else {
            JOptionPane.showMessageDialog(null,"Code incorrecte! ");
        }
    }

    @FXML
    private void returnButtonOnClick(ActionEvent event){
        codeVerifAnchor.setVisible(false);
        signUpAnchor.setVisible(true);
    }

}
