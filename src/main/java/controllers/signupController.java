package controllers;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import services.ServiceUtilisateurs;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class signupController implements Initializable {
    ServiceUtilisateurs serviceUtilisateurs = new ServiceUtilisateurs();

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
    private TextField pseudoSignup, cinSignup, nomSignup, prenomSignup, ageSignup, numtelSignup, emailSignup;
    @FXML
    private PasswordField mdpSignup, confirmMdpSignup;
    @FXML
    private RadioButton roleClientSignup, roleLocateurSignup, roleLivreurSignup;
    @FXML
    private Button signUpButton, loginSignup;
    @FXML
    private Label pseudoSignupError, cinSignupError, nomSignupError, prenomSignupError, ageSignupError, numtelSignupError, emailSignupError, mdpSignupError, confirmMdpSignupError, roleSignupError ;


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
        if(nomSignup.getText().isBlank() || !nomSignup.getText().matches("[a-zA-Z]+")){
            nomSignupError.setTextFill(Color.RED);
            nomSignupError.setText("Le nom est invalide");
            return true;
        }
        if(prenomSignup.getText().isBlank() || !prenomSignup.getText().matches("[a-zA-Z]+")){
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
    public void signUpButtonButtonOnClick(ActionEvent event){
        if (!getErrors()) {
            Utilisateur newUser = new Utilisateur(pseudoSignup.getText(), Integer.parseInt(cinSignup.getText()), nomSignup.getText(),
                    prenomSignup.getText(), Integer.parseInt(ageSignup.getText()), Integer.parseInt(numtelSignup.getText()), emailSignup.getText(),
                    mdpSignup.getText(), (roleClientSignup.isSelected() ? "Client" : (roleLocateurSignup.isSelected() ? "Locateur" : "Livreur")));
            try {
                serviceUtilisateurs.ajouter(newUser);
                System.out.println("Utilisateur ajouté avec succès !");
                serviceUtilisateurs.changeScreen(event, "/login.fxml", "Sign Up avec succées, Connectez vous!");
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            }

        }

    }
}
