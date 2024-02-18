package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import services.ServiceUtilisateurs;

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
    ServiceUtilisateurs serviceUtilisateurs;
    private int index = -1;

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
        if(nomDashboard.getText().isBlank() || !nomDashboard.getText().matches("[a-zA-Z]+")){
            nomDashboardError.setTextFill(Color.RED);
            nomDashboardError.setText("Le nom est invalide");
            return true;
        }
        if(prenomDashboard.getText().isBlank() || !prenomDashboard.getText().matches("[a-zA-Z]+")){
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





}
