package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import entities.Reservation;
import entities.Terrain;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import entities.Equipement;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceEquipement;

import javax.swing.*;
import javafx.collections.transformation.FilteredList;
import services.ServiceTerrain;

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
    private ComboBox<Integer> TerrainIdAjout;

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
    private Button listedesreservations;

    @FXML
    private Label typeError;

    String url;
    private ServiceEquipement serviceEquipement;
    int index = -1;

    private static List<Equipement>deletedEquipements = new ArrayList<>();
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
                    ,Integer.valueOf(prixAjout.getText()),url,Integer.valueOf(stockAjout.getText()),Integer.valueOf(TerrainIdAjout.getValue().toString()));
            try {
                // Vérifier si l'équipement existe déjà dans la base de données
                List<Equipement> equipementsExistant = serviceEquipement.recherche(e.getNom());
                if (!equipementsExistant.isEmpty()) {
                    // L'équipement existe déjà, afficher un message d'erreur
                    JOptionPane.showMessageDialog(null,"Erreur : L'équipement existe déjà dans la base de données !");
                } else {
                    // L'équipement n'existe pas, procéder à l'ajout
                    serviceEquipement.ajouter(e);
                    JOptionPane.showMessageDialog(null,"Equipement Ajoutée avec succés !");
                    refreshTableView();
                }
            } catch (SQLException ex) {
                System.out.println("Erreur lors de l'ajout de l'utilisateur : " + ex.getMessage());
            }
        } else {
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
                    ,Integer.valueOf(prixAjout.getText()),url,Integer.valueOf(stockAjout.getText()),Integer.valueOf(TerrainIdAjout.getValue().toString()));
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



    private void filterTableView(String searchTerm) {
        ObservableList<Equipement> filteredList = FXCollections.observableArrayList();
        for (Equipement equipement : tableEquipements.getItems()) {
            if (equipement.getNom().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredList.add(equipement);
            }
        }
        tableEquipements.setItems(filteredList);
    }


    @FXML
    void supprimerButtonOnClick(ActionEvent event) {
        try {
            List<Equipement> equipementsSelectionnes = tableEquipements.getSelectionModel().getSelectedItems();
            if (equipementsSelectionnes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Aucun utilisateur sélectionné !");
                return;
            }
            int choix = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer ces reservations ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
            if (choix == JOptionPane.YES_OPTION) {
                for (Equipement eq : equipementsSelectionnes) {
                    serviceEquipement.supprimer(eq.getId());
                }
                JOptionPane.showMessageDialog(null, "Utilisateurs supprimés avec succès !");
                refreshTableView();
                System.out.println("Utilisateurs supprimés avec succès !");
            }
        }catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des utilisateurs : " + e.getMessage());
        }
    }




    @FXML
    void initialize() {
        ObservableList<String> types = FXCollections.observableArrayList("Football", "Handball", "Basketball", "Tennis", "Volleyball");
        typeAjout.setItems(types);
        ServiceTerrain serviceTerrain = new ServiceTerrain();
        ObservableList<Terrain> listeTerrains = null;
        try {
            listeTerrains = FXCollections.observableList(serviceTerrain.afficher());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Création d'une liste pour stocker les noms des terrains
        ObservableList<Integer> idTerrains = FXCollections.observableArrayList();
        for (Terrain terrain : listeTerrains) {
            idTerrains.add(terrain.getId());
        }

        // Ajout des noms des terrains à la liste déroulante
        TerrainIdAjout.setItems(idTerrains);
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

        // Créer une FilteredList à partir de la liste observable
        FilteredList<Equipement> filteredList = new FilteredList<>(liste, p -> true);

        // Ajouter un écouteur de modification de propriété sur le champ de recherche
        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(equipement -> {
                // Si le texte de recherche est vide, afficher tous les éléments
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convertir le texte de recherche en minuscules pour une recherche insensible à la casse
                String searchTerm = newValue.toLowerCase();

                // Vérifier si l'équipement correspond au texte de recherche
                return equipement.getNom().toLowerCase().contains(searchTerm);
            });
        });

        // Lier la TableView à la FilteredList
        tableEquipements.setItems(filteredList);

        tableEquipements.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableEquipements.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Equipement> change) -> {
            if (change.getList().size() > 0 && change.getList().get(0) != null && change.getList().get(0).equals(KeyCode.CONTROL)) {
                recupererIds();
            }
        });
    }
    @FXML
    private void recupererIds() {
        List<Equipement> reservationsSelectionnes = tableEquipements.getSelectionModel().getSelectedItems();
        deletedEquipements.clear();
        deletedEquipements.addAll(reservationsSelectionnes);
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

    @FXML
    void navigateToListReservationInterface(ActionEvent event) {


        try {
            // Charger le fichier FXML à partir du chemin relatif
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListReservations.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la vue chargée si nécessaire
            listReservation listReservationController = loader.getController();

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Récupérer la fenêtre principale (stage) à partir de n'importe quel nœud de la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre et l'afficher
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs potentielles lors du chargement du fichier FXML
            e.printStackTrace(); // Vous pouvez également imprimer ou gérer autrement l'erreur
        }
    }

}
