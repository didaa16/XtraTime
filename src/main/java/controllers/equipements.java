package controllers;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import entities.Equipement;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import services.ServiceEquipement;

import javax.swing.*;

public class equipements {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button ajouterButton;

    @FXML
    private TextArea descriptionAjout;

    @FXML
    private TableColumn<Equipement, String> descriptionEquipements;

    @FXML
    private Label descriptionError;

    @FXML
    private TableColumn<Equipement, Integer> idEquipements;

    @FXML
    private ImageView imageAjouter;

    @FXML
    private Label imageError;

    @FXML
    private Button importerImage;

    @FXML
    private Button modifierButton;

    @FXML
    private TextField nomAjout;

    @FXML
    private TableColumn<Equipement, String> nomEquipements;

    @FXML
    private Label nomError;

    @FXML
    private TextField prixAjout;

    @FXML
    private TableColumn<Equipement, Integer> prixEquipements;

    @FXML
    private Label prixError;

    @FXML
    private TextField recherche;

    @FXML
    private Button rechercheButton;

    @FXML
    private TextField stockAjout;

    @FXML
    private TextField idField;

    @FXML
    private TableColumn<Equipement, Integer> stockEquipements;

    @FXML
    private Label stockError;

    @FXML
    private Button supprimerButton;

    @FXML
    private TableView<Equipement> tableEquipements;

    @FXML
    private ComboBox<String> typeAjout;

    @FXML
    private TableColumn<Equipement, String> typeEquipements;

    @FXML
    private Label typeError;

    String url;
    private ServiceEquipement serviceEquipement;
    int index = -1;
    private boolean getErrors(){
        nomError.setText("");
        descriptionError.setText("");
        typeError.setText("");
        stockError.setText("");
        prixError.setText("");
        imageError.setText("");

        boolean errorFound = false;
        if(nomAjout.getText().isBlank() || !nomAjout.getText().matches("[a-zA-Z ]+")){
            nomError.setTextFill(Color.RED);
            nomError.setText("Le nom doit contenir uniquement des caractères alphabétiques et des espaces");
            errorFound = true;
        }
        if(typeAjout.getValue() == null || typeAjout.getValue().toString().isBlank()){
            typeError.setTextFill(Color.RED);
            typeError.setText("Le type est obligatoire");
            errorFound = true;
        }
        if(stockAjout.getText().isBlank() || !stockAjout.getText().matches("\\d+")){
            stockError.setTextFill(Color.RED);
            stockError.setText("Le stock doit être un nombre entier positif");
            errorFound = true;
        }
        if(prixAjout.getText().isBlank() || !prixAjout.getText().matches("\\d+")){
            prixError.setTextFill(Color.RED);
            prixError.setText("Le prix doit être un nombre entier positif");
            errorFound = true;
        }
        return errorFound;
    }

    @FXML
    void ajouterButtonOnClick(ActionEvent event) {
        if (!getErrors()){
            Equipement e = new Equipement(nomAjout.getText(),descriptionAjout.getText(),typeAjout.getValue().toString()
                    ,Integer.valueOf(prixAjout.getText()),url,Integer.valueOf(stockAjout.getText()));
            try {
                serviceEquipement.ajouter(e);
                JOptionPane.showMessageDialog(null,"Equipement Ajoutée avec succés !");
                refreshTableView();
            } catch (SQLException ex) {
                System.out.println("Erreur lors de l'ajout de l'utilisateur : " + ex.getMessage());
            }
        }else {
            imageError.setTextFill(Color.RED);
            imageError.setText("Veullez remplir tous les champs !");
        }
    }

    @FXML
    void importerOnClick(ActionEvent event) {
        FileChooser fileChooser1 = new FileChooser();
        fileChooser1.setTitle("Open Image File");
        File file = fileChooser1.showOpenDialog(null);
        if (file != null) {
            String absolutePath = file.getAbsolutePath();
            Image selectedImage = new Image(new File(absolutePath).toURI().toString());
            javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
            imageAjouter.setImage(image);
            url=absolutePath;
        }
    }

    @FXML
    void modifierButtonOnClick(ActionEvent event) {
        if (!getErrors()){
            Equipement e = new Equipement(Integer.valueOf(idField.getText()), nomAjout.getText(),descriptionAjout.getText(),typeAjout.getValue().toString()
                    ,Integer.valueOf(prixAjout.getText()),url,Integer.valueOf(stockAjout.getText()));
            try {
                serviceEquipement.modifier(e);
                JOptionPane.showMessageDialog(null,"Equipement Modifiée avec succès !");
                // Rafraîchir la TableView après la modification
                refreshTableView();
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la modification de l'équipement : " + ex.getMessage());
            }
        } else {
            imageError.setTextFill(Color.RED);
            imageError.setText("Veuillez remplir tous les champs !");
        }
    }



    @FXML
    void rechercheButtonOnClick(ActionEvent event) {
        String termeRecherche = recherche.getText();
        ObservableList<Equipement> liste = null;
        try {
            if (recherche.getText().isBlank()){
                liste = FXCollections.observableList(serviceEquipement.afficher());
            }else {
                liste = FXCollections.observableList(serviceEquipement.recherche(termeRecherche));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        idEquipements.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomEquipements.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionEquipements.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeEquipements.setCellValueFactory(new PropertyValueFactory<>("type"));
        stockEquipements.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prixEquipements.setCellValueFactory(new PropertyValueFactory<>("prix"));
        tableEquipements.setItems(liste);
    }




    @FXML
    void supprimerButtonOnClick(ActionEvent event) {
        if (index != -1) {
            Equipement equipementSelectionne = tableEquipements.getItems().get(index);
            try {
                serviceEquipement.supprimer(equipementSelectionne.getId());
                JOptionPane.showMessageDialog(null,"Equipement Supprimé avec succès !");
                refreshTableView(); // Rafraîchir la TableView après la suppression
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la suppression de l'équipement : " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null,"Veuillez sélectionner un équipement à supprimer !");
        }
    }




    @FXML
    void initialize() {
        ObservableList<String> types = FXCollections.observableArrayList("Football", "Handball", "Basketball", "Tennis", "Volleyball");
        typeAjout.setItems(types);
        serviceEquipement = new ServiceEquipement();
        ObservableList<Equipement> liste = null;
        try {
            liste = FXCollections.observableList(serviceEquipement.afficher());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        idEquipements.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomEquipements.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionEquipements.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeEquipements.setCellValueFactory(new PropertyValueFactory<>("type"));
        stockEquipements.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prixEquipements.setCellValueFactory(new PropertyValueFactory<>("prix"));
        tableEquipements.setItems(liste);
    }

    @FXML
    void getSelected(MouseEvent event) {
        index = tableEquipements.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        Equipement equipementSelectionnee = tableEquipements.getItems().get(index);
        idField.setText(String.valueOf(equipementSelectionnee.getId()));
        nomAjout.setText(String.valueOf(equipementSelectionnee.getNom()));
        descriptionAjout.setText(equipementSelectionnee.getDescription());
        typeAjout.setValue(equipementSelectionnee.getType());
        stockAjout.setText(String.valueOf(equipementSelectionnee.getStock()));
        prixAjout.setText(String.valueOf(equipementSelectionnee.getPrix()));

    }

    private void refreshTableView() {
        ObservableList<Equipement> liste = null;
        try {
            liste = FXCollections.observableList(serviceEquipement.afficher());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        tableEquipements.setItems(liste);
    }

}
