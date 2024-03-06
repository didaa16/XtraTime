package controllers.event;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.event.sponso;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import services.event.sponsoService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.scene.image.Image;



public class sponsoDashboard {

    private Image image;
    private String imageUrl;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button bafficher;

    @FXML
    private Button bajouter;
    @FXML
    private AnchorPane bb;

    @FXML
    private Button bimporter;

    @FXML
    private Button bmodifier;

    @FXML
    private Button bsupprimer;

    @FXML
    private TableColumn<?, ?> colimage;

    @FXML
    private TableColumn<sponso, String> colmail;

    @FXML
    private TableColumn<sponso, Integer> coltel;

    @FXML
    private TableColumn<sponso, String> colnom;

    @FXML
    private TextField tfmail;


    @FXML
    private TextField tfnom;

    @FXML
    private TextField tftel;

    @FXML
    private TableView<sponso> tvsponso;
    @FXML
    private ImageView imageimport;
    @FXML
    private TextField searchTF;


    private void refreshTableView() {
        ObservableList<sponso> sponsors = FXCollections.observableList(ss.readAll());
        tvsponso.setItems(sponsors);
    }
    @FXML
    void afficher(ActionEvent event) {


    }


    @FXML
    void ajouter(ActionEvent event) {
        // Check if any required field is empty
        if (tfnom.getText().isEmpty() || tftel.getText().isEmpty() || tfmail.getText().isEmpty()) {
            Alert missingFieldAlert = new Alert(Alert.AlertType.WARNING);
            missingFieldAlert.setTitle("Missing Information");
            missingFieldAlert.setHeaderText(null);
            missingFieldAlert.setContentText("Please fill in all required fields!");
            missingFieldAlert.showAndWait();
            return; // Return from the method if any required field is empty
        }

        // Check if an image is selected
        if (image == null || imageUrl == null || imageUrl.isEmpty()) {
            Alert missingImageAlert = new Alert(Alert.AlertType.WARNING);
            missingImageAlert.setTitle("Missing Image");
            missingImageAlert.setHeaderText(null);
            missingImageAlert.setContentText("Please select an image!");
            missingImageAlert.showAndWait();
            return; // Return from the method if no image is selected
        }

        // Convert the input values to appropriate data types
        String nom = tfnom.getText();
        String mail = tfmail.getText();
        int tel = Integer.parseInt(tftel.getText()); // Assuming tel is an integer
        // Check if the sponsor with the given name already exists
        if (ss.sponsorExistsByName(nom)) {
            Alert existingSponsorAlert = new Alert(Alert.AlertType.WARNING);
            existingSponsorAlert.setTitle("Commanditaire en double");
            existingSponsorAlert.setHeaderText(null);
            existingSponsorAlert.setContentText("Un sponsor du même nom existe déjà !");
            existingSponsorAlert.showAndWait();
            return; // Return from the method if sponsor with the same name exists
        }

        // Check if the sponsor with the given tel already exists
        if (ss.sponsorExistsByTel(tel)) {
            Alert existingSponsorAlert = new Alert(Alert.AlertType.WARNING);
            existingSponsorAlert.setTitle("Duplicate Sponsor");
            existingSponsorAlert.setHeaderText(null);
            existingSponsorAlert.setContentText("Un sponsor du même Numero de telephone existe déjà !");
            existingSponsorAlert.showAndWait();
            return; // Return from the method if sponsor with the same tel exists
        }
        // Check if the selected image already exists
        if (ss.imageExists(imageUrl)) {
            Alert imageExistsAlert = new Alert(Alert.AlertType.WARNING);
            imageExistsAlert.setTitle("Image double");
            imageExistsAlert.setHeaderText(null);
            imageExistsAlert.setContentText("Un sponsor avec la même image existe déjà !");
            imageExistsAlert.showAndWait();
            return; // Return from the method if the image already exists
        }
        // Check if the sponsor with the given email already exists
        if (ss.sponsorExistsByEmail(mail)) {
            Alert existingSponsorAlert = new Alert(Alert.AlertType.WARNING);
            existingSponsorAlert.setTitle("Duplicate Sponsor");
            existingSponsorAlert.setHeaderText(null);
            existingSponsorAlert.setContentText("Un sponsor avec le même email existe déjà !");
            existingSponsorAlert.showAndWait();
            return; // Return from the method if sponsor with the same email exists
        }

        // Create a new sponso object
        if (verifNom()&& verifMail(mail) && verifTelephone(tel)){
        sponso newSponsor = new sponso(nom, tel, mail, imageUrl);

        // Call the add method in the sponsoService to add the new sponsor to the database
        ss.add(newSponsor);

        // Show a success message
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Sponsor Added");
        successAlert.setHeaderText(null);
        successAlert.setContentText("The sponsor has been added successfully!");
        successAlert.showAndWait();

        // Clear input fields
        tfnom.clear();
        tfmail.clear();
        tftel.clear();
        imageimport.setImage(null);


        // Refresh TableView
        refreshTableView();
    }
    }






    @FXML
    void importer(ActionEvent event) {
        // Create a file chooser dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");

        // Set the file extension filters if needed (e.g., for image files only)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Show the file chooser dialog and wait for the user to select a file
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Set the selected file path to the class-level variable
            imageUrl = selectedFile.toURI().toString();

            image=new Image(selectedFile.toURI().toString(), 330, 210, false, true);
            imageimport.setImage(image);
        }
        System.out.println(selectedFile);


    }





        @FXML
        private void modifier(ActionEvent event){
        // Get the selected sponsor from the TableView
    sponso selectedSponsor = tvsponso.getSelectionModel().getSelectedItem();
        if (selectedSponsor == null) {
        // If no sponsor is selected, show a warning message and return
        Alert noSponsorSelectedAlert = new Alert(Alert.AlertType.WARNING);
        noSponsorSelectedAlert.setTitle("No Sponsor Selected");
        noSponsorSelectedAlert.setHeaderText(null);
        noSponsorSelectedAlert.setContentText("Please select a sponsor to modify.");
        noSponsorSelectedAlert.showAndWait();
        return;
    }

    // Check if any required field is empty
        if (tfnom.getText().isEmpty() || tftel.getText().isEmpty() || tfmail.getText().isEmpty()) {
        Alert missingFieldAlert = new Alert(Alert.AlertType.WARNING);
        missingFieldAlert.setTitle("Missing Information");
        missingFieldAlert.setHeaderText(null);
        missingFieldAlert.setContentText("Please fill in all required fields!");
        missingFieldAlert.showAndWait();
        return; // Return from the method if any required field is empty
    }
            // Retain the existing image URL if no new image is selected
            if (imageUrl == null || imageUrl.isEmpty()) {
                imageUrl = selectedSponsor.getImage();
            }
    // Convert the input values to appropriate data types
    String nom = tfnom.getText();
    String mail = tfmail.getText();
    int tel = Integer.parseInt(tftel.getText()); // Assuming tel is an integer


    // Update the selected sponsor object
        selectedSponsor.setNom(nom);
        selectedSponsor.setEmail(mail);
        selectedSponsor.setTel(tel);
        selectedSponsor.setImage(imageUrl);

    // Call the update method in sponsoService to update the sponsor in the database
        ss.update(selectedSponsor);

    // Show a success message
    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Sponsor Updated");
        successAlert.setHeaderText(null);
        successAlert.setContentText("The sponsor has been updated successfully!");
        successAlert.showAndWait();

    // Clear input fields
        tfnom.clear();
        tfmail.clear();
        tftel.clear();
        imageimport.setImage(null);


    // Refresh TableView
    refreshTableView();
    }






    @FXML
    void supprimer(ActionEvent event) {

        ObservableList<sponso> selectedSponsors = tvsponso.getSelectionModel().getSelectedItems();

        if (selectedSponsors.isEmpty()) {
            Alert noSponsorsSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSponsorsSelectedAlert.setTitle("No Sponsors Selected");
            noSponsorsSelectedAlert.setHeaderText(null);
            noSponsorsSelectedAlert.setContentText("Please select one or more sponsors to delete.");
            noSponsorsSelectedAlert.showAndWait();
            return;
        }

        Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDeleteAlert.setTitle("Confirm Deletion");
        confirmDeleteAlert.setHeaderText("Delete Sponsor(s)");
        confirmDeleteAlert.setContentText("Are you sure you want to delete the selected sponsor(s)?");
        ButtonType confirmButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmDeleteAlert.getButtonTypes().setAll(confirmButton, cancelButton);
        Optional<ButtonType> result = confirmDeleteAlert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            // Delete each selected sponsor
            for (sponso selectedSponsor : selectedSponsors) {
                ss.delete(selectedSponsor);
            }

            // Show a success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Sponsor(s) Deleted");
            successAlert.setHeaderText(null);
            successAlert.setContentText("The selected sponsor(s) have been deleted successfully!");
            successAlert.showAndWait();

            // Refresh TableView
            refreshTableView();
        }
    }

    sponsoService ss =new sponsoService();

    @FXML
    void initialize() {
        tvsponso.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ObservableList<sponso> sponsors = FXCollections.observableList(ss.readAll());
        tvsponso.setItems(sponsors);
        colnom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        coltel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colimage.setCellValueFactory(new PropertyValueFactory<>("image"));



    }

    @FXML
    public void slecteddata(javafx.scene.input.MouseEvent mouseEvent) {
        sponso selectedSponsor = tvsponso.getSelectionModel().getSelectedItem();
        if (selectedSponsor != null) {
            tfnom.setText(selectedSponsor.getNom());
            tfmail.setText(selectedSponsor.getEmail());
            tftel.setText(String.valueOf(selectedSponsor.getTel()));
            // Assuming colimage is a TableColumn for the image in the TableView
            // You can set the image path to the text field or ImageView as needed
            // For example, setting the image path to a TextField:
            // txtimage.setText(selectedSponsor.getImage());
            // Or setting the image to an ImageView:
            String imagePath = selectedSponsor.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = new Image(imagePath, 330, 210, false, true);
                System.out.print(":+..........."+imagePath);
                imageimport.setImage(image);
            } else {
                // If the image path is empty or null, clear the ImageView
                imageimport.setImage(null);
            }
        }
    }

    ///////Controle de saisie

    private boolean verifMail(String email) {
        // Regular expression for email validation
        Pattern p = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher m = p.matcher(tfmail.getText());
        if (m.find() && m.group().equals(tfmail.getText())) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation des champs");
            alert.setHeaderText(null);
            alert.setContentText("Verifier l'adresse mail ' , mail invalid ");
            alert.showAndWait();
            return false;
        }


    }

    private boolean verifNom() {
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(tfnom.getText());
        if (m.find() && m.group().equals(tfnom.getText())) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation des champs");
            alert.setHeaderText(null);
            alert.setContentText("Verifier le Nom du sponsor , il ne doit pas contenir des chiffres");
            alert.showAndWait();
            return false;
        }
    }

    private boolean verifTelephone(int telephone) {
        // Convertir le numéro de téléphone en une chaîne de caractères
        String telString = String.valueOf(telephone);

        // Vérifier si le numéro de téléphone contient exactement 8 chiffres
        if (telString.length() == 8) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation du numéro de téléphone");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir un numéro de téléphone valide contenant exactement 8 chiffres.");
            alert.showAndWait();
            return false;
        }
    }

    ObservableList<sponso> sponsors = FXCollections.observableList(ss.readAll());

    @FXML
    void filter(ActionEvent event) {
        ObservableList<sponso> filteredSponsors = FXCollections.observableArrayList();

        // Filter sponsors based on their names
        filteredSponsors.addAll(sponsors.stream()
                .filter(sponsor -> sponsor.getNom().toLowerCase().contains(searchTF.getText().toLowerCase()))
                .collect(Collectors.toList()));

        // Update the TableView with the filtered list
        tvsponso.setItems(filteredSponsors);
    }




}

