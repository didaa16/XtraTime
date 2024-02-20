package controllers;

import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import entities.Encryptor;
import entities.Utilisateur;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import services.ServiceUtilisateurs;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;

public class dashboard {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Utilisateur> TableView;

    @FXML
    private TableColumn<Utilisateur, Integer> ageC;

    @FXML
    private TextField ageDashboard;

    @FXML
    private Label ageDashboardError;

    @FXML
    private TableColumn<Utilisateur, Integer> cinC;

    @FXML
    private TextField cinDashboard;

    @FXML
    private Label cinDashboardError;

    @FXML
    private Button deconnecterButton;

    @FXML
    private TableColumn<Utilisateur, String> emailC;

    @FXML
    private TextField emailDashboard;

    @FXML
    private Label emailDashboardError;

    @FXML
    private Button modifierButton;

    @FXML
    private TableColumn<Utilisateur, Integer> nomC;

    @FXML
    private TextField nomDashboard;

    @FXML
    private Label nomDashboardError;

    @FXML
    private TableColumn<Utilisateur, Integer> numTelC;

    @FXML
    private TextField numtelDashboard;

    @FXML
    private Label numtelDashboardError;

    @FXML
    private TableColumn<Utilisateur, String> prenomC;

    @FXML
    private TextField prenomDashboard;

    @FXML
    private Label prenomDashboardError;

    @FXML
    private Button profileButton;

    @FXML
    private TableColumn<Utilisateur, String> pseudoC;

    @FXML
    private TextField pseudoDashboard;

    @FXML
    private Label pseudoDashboardError;

    @FXML
    private Button rechercheButton;

    @FXML
    private TextField rechercheDashboard;

    @FXML
    private ToggleGroup role;

    @FXML
    private RadioButton roleClientDashboard;

    @FXML
    private Label roleDashboardError;

    @FXML
    private RadioButton roleLivreurDashboard;

    @FXML
    private RadioButton roleLocateurDashboard;

    @FXML
    private ToggleGroup roles;

    @FXML
    private Button supprimerButton;
    @FXML
    private RadioButton admins;
    @FXML
    private RadioButton clients;
    @FXML
    private RadioButton locateurs;
    @FXML
    private RadioButton livreurs;
    @FXML
    private Button Menu;
    @FXML
    private Button MenuClose;
    @FXML
    private AnchorPane slider;
    @FXML
    private AnchorPane utilisateursAnchorPane;
    @FXML
    private AnchorPane ajouterAnchorPane;
    @FXML
    private Button utilisateursButton;
    @FXML
    private TextField pseudoAjout, cinAjout, nomAjouter, prenomAjout, ageAjout, numtelAjout, emailAjout, mdpAjout;
    @FXML
    private PasswordField confirmMdpAjout;
    @FXML
    private Label pseudoError, cinError, nomError, prenomError, ageError, numtelError, emailError, mdpError, confirmMdpError, mdpLabel, confirmMdpLabel;
    @FXML
    private Button annulerButton, ajoutAdminButton;
    ServiceUtilisateurs serviceUtilisateurs;
    Encryptor encryptor = new Encryptor();
    private int index = -1;
    private static Utilisateur loggedInUser;

    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }

    void updateData(){
        pseudoDashboard.setDisable(true);
        serviceUtilisateurs = new ServiceUtilisateurs();
        afficherUtilisateursParRole("Admin");
        admins.setOnAction(event -> afficherUtilisateursParRole("Admin"));
        clients.setOnAction(event -> afficherUtilisateursParRole("Client"));
        locateurs.setOnAction(event -> afficherUtilisateursParRole("Locateur"));
        livreurs.setOnAction(event -> afficherUtilisateursParRole("Livreur"));
    }
    @FXML
    void initialize() {
        deconnecterButton.setOnMouseClicked(event -> {
            System.exit(0);
        });
        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        MenuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });
        updateData();

    }

    private void afficherUtilisateursParRole(String role) {
        ObservableList<Utilisateur> listeUtilisateurs = FXCollections.observableList(serviceUtilisateurs.afficherParRole(role));
        pseudoC.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
        cinC.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomC.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomC.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageC.setCellValueFactory(new PropertyValueFactory<>("age"));
        numTelC.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        emailC.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableView.setItems(listeUtilisateurs);
    }
    @FXML
    void getSelected(MouseEvent event) {
        index = TableView.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        Utilisateur utilisateurSelectionne = TableView.getItems().get(index);
        pseudoDashboard.setText(utilisateurSelectionne.getPseudo());
        cinDashboard.setText(String.valueOf(utilisateurSelectionne.getCin()));
        nomDashboard.setText(utilisateurSelectionne.getNom());
        prenomDashboard.setText(utilisateurSelectionne.getPrenom());
        ageDashboard.setText(String.valueOf(utilisateurSelectionne.getAge()));
        numtelDashboard.setText(String.valueOf(utilisateurSelectionne.getNumtel()));
        emailDashboard.setText(utilisateurSelectionne.getEmail());
        switch (utilisateurSelectionne.getRole()){
            case "Admin":
                roleClientDashboard.setDisable(true);
                roleLocateurDashboard.setDisable(true);
                roleLivreurDashboard.setDisable(true);
                break;
            case "Client":
                roleClientDashboard.setDisable(false);
                roleLocateurDashboard.setDisable(false);
                roleLivreurDashboard.setDisable(false);
                roleClientDashboard.setSelected(true);
                break;
            case "Locateur":
                roleClientDashboard.setDisable(false);
                roleLocateurDashboard.setDisable(false);
                roleLivreurDashboard.setDisable(false);
                roleLocateurDashboard.setSelected(true);
                break;
            case "Livreur":
                roleClientDashboard.setDisable(false);
                roleLocateurDashboard.setDisable(false);
                roleLivreurDashboard.setDisable(false);
                roleLivreurDashboard.setSelected(true);
                break;
            default:
                break;
        }
    }
    public boolean getErrors(){
        pseudoDashboardError.setText("");
        cinDashboardError.setText("");
        nomDashboardError.setText("");
        prenomDashboardError.setText("");
        ageDashboardError.setText("");
        numtelDashboardError.setText("");
        emailDashboardError.setText("");
        roleDashboardError.setText("");
        if(pseudoDashboard.getText().isBlank()){
            pseudoDashboardError.setTextFill(Color.RED);
            pseudoDashboardError.setText("Le Pseudo est invalide");
            return true;
        }
        if(cinDashboard.getText().isBlank() || !cinDashboard.getText().matches("\\d{1,9}")){
            cinDashboardError.setTextFill(Color.RED);
            cinDashboardError.setText("Le CIN est invalide");
            return true;
        }
        if(nomDashboard.getText().isBlank() || !nomDashboard.getText().matches("[a-zA-Z ]+")){
            nomDashboardError.setTextFill(Color.RED);
            nomDashboardError.setText("Le nom est invalide");
            return true;
        }

        if(prenomDashboard.getText().isBlank() || !prenomDashboard.getText().matches("[a-zA-Z ]+")){
            prenomDashboardError.setTextFill(Color.RED);
            prenomDashboardError.setText("Le prénom est invalide");
            return true;
        }
        if(ageDashboard.getText().isBlank() || !ageDashboard.getText().matches("\\d+") || Integer.parseInt(ageDashboard.getText()) < 18){
            ageDashboardError.setTextFill(Color.RED);
            ageDashboardError.setText("L'âge doit être un nombre valide et être supérieur à 18");
            return true;
        }
        if(numtelDashboard.getText().isBlank() || !numtelDashboard.getText().matches("\\d{1,12}")){
            numtelDashboardError.setTextFill(Color.RED);
            numtelDashboardError.setText("Le numéro de téléphone est invalide");
            return true;
        }
        if(emailDashboard.getText().isBlank() || !emailDashboard.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            emailDashboardError.setTextFill(Color.RED);
            emailDashboardError.setText("L'email est invalide");
            return true;
        }
        if(!roleClientDashboard.isSelected() && !roleLocateurDashboard.isSelected() && !roleLivreurDashboard.isSelected()){
            roleDashboardError.setTextFill(Color.RED);
            roleDashboardError.setText("Le role est obligatoire");
            return true;
        }
        return false;
    }

    @FXML
    public void ModifierButtonButtonOnClick(ActionEvent event){
        if (!getErrors()) {
            try {
                Utilisateur newUser1 = serviceUtilisateurs.afficherParPseudo(pseudoDashboard.getText());
                Utilisateur newUser = new Utilisateur(newUser1.getPseudo(), Integer.parseInt(cinDashboard.getText()), nomDashboard.getText(),
                        prenomDashboard.getText(), Integer.parseInt(ageDashboard.getText()), Integer.parseInt(numtelDashboard.getText()),
                        emailDashboard.getText(), newUser1.getMdp(), (roleClientDashboard.isSelected() ? "Client" : (roleLocateurDashboard.isSelected() ? "Locateur" : "Livreur")));
                serviceUtilisateurs.modifier(newUser);
                JOptionPane.showMessageDialog(null,"Modification effectuée! ");
                updateData();
                switch (newUser.getRole()){
                    case "Admin":
                        admins.setSelected(true);
                        break;
                    case "Client":
                        clients.setSelected(true);
                        break;
                    case "Locateur":
                        locateurs.setSelected(true);
                        break;
                    case "Livreur":
                        livreurs.setSelected(true);
                        break;
                    default:
                        break;
                }
                afficherUtilisateursParRole(newUser.getRole());
                TableView.refresh();
                System.out.println("Utilisateur modifié avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
            }

        }
    }
    @FXML
    public void SupprimerButtonButtonOnClick(ActionEvent event){
        try {
            Utilisateur newUser = serviceUtilisateurs.afficherParPseudo(pseudoDashboard.getText());
            int choix = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer cet utilisateur ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

            if (choix == JOptionPane.YES_OPTION) {
                serviceUtilisateurs.supprimer(newUser);
                JOptionPane.showMessageDialog(null,"Utilisateur supprimé avec succès ! ");
                updateData();
                afficherUtilisateursParRole(newUser.getRole());
                TableView.refresh();
                System.out.println("Utilisateur supprimé avec succès !");
            } else {
                System.out.println("Suppression annulée par l'utilisateur.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }
    @FXML
    public void rechercheButtonOnClick(ActionEvent event){
        String data = rechercheDashboard.getText().toString();
        admins.setSelected(false);
        clients.setSelected(false);
        locateurs.setSelected(false);
        livreurs.setSelected(false);
        ObservableList<Utilisateur> listeUtilisateurs = FXCollections.observableList(serviceUtilisateurs.recherche(data));
        pseudoC.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
        cinC.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomC.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomC.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageC.setCellValueFactory(new PropertyValueFactory<>("age"));
        numTelC.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        emailC.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableView.setItems(listeUtilisateurs);
    }

    @FXML
    private void utilisateursButtonOnClick(ActionEvent event){
        utilisateursAnchorPane.setVisible(true);
        ajouterAnchorPane.setVisible(false);
    }
    @FXML
    private void ajouterButtonOnClick(ActionEvent event){
        utilisateursAnchorPane.setVisible(false);
        ajouterAnchorPane.setVisible(true);
    }
    public boolean getErrors1(){
        pseudoError.setText("");
        cinError.setText("");
        nomError.setText("");
        prenomError.setText("");
        ageError.setText("");
        numtelError.setText("");
        emailError.setText("");
        mdpError.setText("");
        confirmMdpError.setText("");

        if(pseudoAjout.getText().isBlank()){
            pseudoError.setTextFill(Color.RED);
            pseudoError.setText("Le Pseudo est invalide");
            return true;
        }
        if(cinAjout.getText().isBlank() || !cinAjout.getText().matches("\\d{1,9}")){
            cinError.setTextFill(Color.RED);
            cinError.setText("Le CIN est invalide");
            return true;
        }
        if(nomAjouter.getText().isBlank() || !nomAjouter.getText().matches("[a-zA-Z ]+")){
            nomError.setTextFill(Color.RED);
            nomError.setText("Le nom est invalide");
            return true;
        }

        if(prenomAjout.getText().isBlank() || !prenomAjout.getText().matches("[a-zA-Z ]+")){
            prenomError.setTextFill(Color.RED);
            prenomError.setText("Le prénom est invalide");
            return true;
        }
        if(ageAjout.getText().isBlank() || !ageAjout.getText().matches("\\d+") || Integer.parseInt(ageAjout.getText()) < 18){
            ageError.setTextFill(Color.RED);
            ageError.setText("L'âge doit être un nombre valide et être supérieur à 18");
            return true;
        }
        if(numtelAjout.getText().isBlank() || !numtelAjout.getText().matches("\\d{1,12}")){
            numtelError.setTextFill(Color.RED);
            numtelError.setText("Le numéro de téléphone est invalide");
            return true;
        }
        if(emailAjout.getText().isBlank() || !emailAjout.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            emailError.setTextFill(Color.RED);
            emailError.setText("L'email est invalide");
            return true;
        }
        if(mdpAjout.getText().isBlank()|| mdpAjout.getText().length() < 8 || mdpAjout.getText().matches("[^a-zA-Z0-9]")){
            mdpError.setTextFill(Color.RED);
            mdpError.setText("Le mot de passe est invalide");
            return true;
        }
        if(confirmMdpAjout.getText().isBlank()){
            confirmMdpError.setTextFill(Color.RED);
            confirmMdpError.setText("La confirmation du mot de passe est invalide");
            return true;
        }
        if(!Objects.equals(confirmMdpAjout.getText(), mdpAjout.getText())){
            confirmMdpError.setTextFill(Color.RED);
            confirmMdpError.setText("Le mot de passe doit etre le meme");
            return true;
        }
        try {
            if (serviceUtilisateurs.pseudoExiste(pseudoAjout.getText())){
                pseudoError.setTextFill(Color.RED);
                pseudoError.setText("Ce pseudo est déjà utilisé, veuillez en choisir un autre");
                return true;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    @FXML
    private void ajoutAdminButtonOnClick(ActionEvent event) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (!getErrors1()) {
            Utilisateur newUser = new Utilisateur(pseudoAjout.getText(), Integer.parseInt(cinAjout.getText()), nomAjouter.getText(),
                    prenomAjout.getText(), Integer.parseInt(ageAjout.getText()), Integer.parseInt(numtelAjout.getText()), emailAjout.getText(),
                    encryptor.encryptString(mdpAjout.getText()), "Admin");
            try {
                serviceUtilisateurs.ajouter(newUser);
                System.out.println("Admin ajouté avec succès !");
                JOptionPane.showMessageDialog(null,"Admin ajouté avec succès !");
                updateData();
                TableView.refresh();
                ajouterAnchorPane.setVisible(false);
                utilisateursAnchorPane.setVisible(true);
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            }

        }
    }
    @FXML
    private void profileButtonOnClick(ActionEvent event){
        utilisateursAnchorPane.setVisible(false);
        ajouterAnchorPane.setVisible(true);
        pseudoAjout.setDisable(true);
        cinAjout.setDisable(true);
        nomAjouter.setDisable(true);
        prenomAjout.setDisable(true);
        ageAjout.setDisable(true);
        numtelAjout.setDisable(true);
        emailAjout.setDisable(true);
        mdpAjout.setDisable(true);
        confirmMdpAjout.setDisable(true);
        pseudoAjout.setText(loggedInUser.getPseudo());
        cinAjout.setText(String.valueOf(loggedInUser.getCin()));
        nomAjouter.setText(loggedInUser.getNom());
        prenomAjout.setText(loggedInUser.getPrenom());
        ageAjout.setText(String.valueOf(loggedInUser.getAge()));
        numtelAjout.setText(String.valueOf(loggedInUser.getNumtel()));
        emailAjout.setText(loggedInUser.getEmail());
        mdpLabel.setVisible(false);
        confirmMdpLabel.setVisible(false);
        mdpAjout.setVisible(false);
        confirmMdpAjout.setVisible(false);
        ajoutAdminButton.setVisible(false);
        annulerButton.setVisible(false);
    }
}
