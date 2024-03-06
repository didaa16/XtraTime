package controllers.event;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Date;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.beans.property.SimpleStringProperty;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import entities.event.event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.event.eventService;
import services.event.sponsoService;
import utils.event.DataSource;


import javax.swing.*;

public class eventDashboard {
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
    private Button bimporter;

    @FXML
    private Button bmodifier;

    @FXML
    private Button bsupprimer;
    eventService es = new eventService();
    sponsoService sp = new sponsoService();

    @FXML
    private TableColumn<event, Timestamp> coldebut;

    @FXML
    private TableColumn<event, String> coldescrition;

    @FXML
    private TableColumn<event, Timestamp> colfin;

    @FXML
    private TableColumn<event, String> colimage;

    @FXML
    private TableColumn<event, String> coltitre;
    @FXML
    private TableColumn<event, String> colsponso;

    @FXML
    private TableColumn<event, String> colterrain;
    @FXML
    private ImageView imageimport;

    @FXML
    private DatePicker tfdd;

    @FXML
    private TextField tfdescription;
    @FXML
    private TextField tfhd;

    @FXML
    private TextField tfhf;

    @FXML
    private DatePicker tfdf;

    @FXML
    private ComboBox<String> tfsponso;

    @FXML
    private ComboBox<String> tfterrain;

    @FXML
    private TextField tftitre;

    @FXML
    private TableView<event> tvevent;
    public ObservableList<event> data = FXCollections.observableArrayList();
    @FXML
    private TextField searchTF;
    ObservableList<event> events = FXCollections.observableList(es.readAll());




    @FXML
    void ajouter(ActionEvent event) {
        // Check if any required field is empty
        if (tfsponso.getSelectionModel().isEmpty() || tfterrain.getSelectionModel().isEmpty() ||
                tftitre.getText().isEmpty() || tfhd.getText().isEmpty() || tfhf.getText().isEmpty() ||
                tfdescription.getText().isEmpty() ||
                tfdd.getValue() == null) {

            Alert missingFieldAlert = new Alert(Alert.AlertType.WARNING);
            missingFieldAlert.setTitle("Information manquante");
            missingFieldAlert.setHeaderText(null);
            missingFieldAlert.setContentText("Veuillez remplir tous les champs requis!");
            missingFieldAlert.showAndWait();
            return; // Return from the method if any required field is empty
        }
        // Check if an image is selected
        if (image == null || imageUrl == null || imageUrl.isEmpty()) {
            Alert missingImageAlert = new Alert(Alert.AlertType.WARNING);
            missingImageAlert.setTitle("Image manquante");
            missingImageAlert.setHeaderText(null);
            missingImageAlert.setContentText("Veuillez sélectionner une image!");
            missingImageAlert.showAndWait();
            return; // Return from the method if no image is selected
        }

        // Convert the selected values to appropriate data types

        int idSponso = sp.getSponsorIdByName(tfsponso.getValue());
        System.out.print(idSponso);
        int idTerrain = es.getterrainIdByName(tfterrain.getValue());
        String titre = tftitre.getText();
        String description = tfdescription.getText();
        // Timestamp dated = Timestamp.valueOf(tfdd.getValue().atTime(LocalTime.parse(tfhd.getText())));
        // Timestamp datef = Timestamp.valueOf(tfdf.getValue().atTime(LocalTime.parse(tfhf.getText())));
        LocalDate startDate = tfdd.getValue();
        LocalTime startTime = LocalTime.parse(tfhd.getText());
        LocalTime endTime = LocalTime.parse(tfhf.getText());

        Timestamp dated = Timestamp.valueOf(LocalDateTime.of(startDate, startTime));
        Timestamp datef = Timestamp.valueOf(LocalDateTime.of(startDate, endTime));


        // Create a new event object
        if (verifTitre() && verifDatedebut()) {
            event newEvent = new event(titre, description, imageUrl, dated, datef, idTerrain, idSponso, "boh");
            if (es.eventExistsWithSameTerrainAndDates(tfterrain.getValue(), dated, datef)) {
                Alert eventExistsAlert = new Alert(Alert.AlertType.WARNING);
                eventExistsAlert.setTitle("\n" +
                        "Événement  double");
                eventExistsAlert.setHeaderText(null);
                eventExistsAlert.setContentText("Un événement avec le même nom de terrain et les mêmes dates existe déjà !");
                eventExistsAlert.showAndWait();
                return; // Retourne de la méthode si un événement avec le même nom de terrain et les mêmes dates existe déjà
            }
            // Check if the selected image already exists
            if (es.imageExists(imageUrl)) {
                Alert imageExistsAlert = new Alert(Alert.AlertType.WARNING);
                imageExistsAlert.setTitle("Image double");
                imageExistsAlert.setHeaderText(null);
                imageExistsAlert.setContentText("Un événement avec la même image existe déjà !");
                imageExistsAlert.showAndWait();
                return; // Return from the method if the image already exists
            }

            // Vérifier si un événement avec le même nom existe déjà
            if (es.eventExistsWithSameName(tftitre.getText())) {
                Alert eventExistsAlert = new Alert(Alert.AlertType.WARNING);
                eventExistsAlert.setTitle(" \n" +
                        "Événement  double");
                eventExistsAlert.setHeaderText(null);
                eventExistsAlert.setContentText("Un événement du même titre existe déjà !");
                eventExistsAlert.showAndWait();
                return;
            }
            // Call the add method in the eventService to add the new event to the database
            es.add(newEvent);

            // Show a success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Événement ajouté");
            successAlert.setHeaderText(null);
            successAlert.setContentText("L'événement a été ajouté avec succès !");
            successAlert.showAndWait();

            // Clear input fields
            tftitre.clear();
            tfdescription.clear();
            tfdd.getEditor().clear();
            tfsponso.setValue(null);
            tfterrain.setValue(null);
            imageimport.setImage(null);
            tfhd.clear();
            tfhf.clear();

            // Refresh TableView to display the newly added event
            refreshTableView();
        }
    }

    private void refreshTableView() {
        ObservableList<event> events = FXCollections.observableList(es.readAll());
        tvevent.setItems(events);
    }


    @FXML
    private void modifier(ActionEvent event) {
        // Get the selected event from the TableView
        event selectedEvent = tvevent.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            // If no event is selected, show a warning message and return
            Alert noEventSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noEventSelectedAlert.setTitle("Aucun événement sélectionné");
            noEventSelectedAlert.setHeaderText(null);
            noEventSelectedAlert.setContentText("Veuillez sélectionner un événement à modifier.");
            noEventSelectedAlert.showAndWait();
            return;
        }

        // Check if any required field is empty
        if (tftitre.getText().isEmpty() || tfdescription.getText().isEmpty() || tfdd.getValue() == null ||
                tfsponso.getValue() == null || tfterrain.getValue() == null
                || tfhd.getText().isEmpty() || tfhf.getText().isEmpty()) {
            Alert missingFieldAlert = new Alert(Alert.AlertType.WARNING);
            missingFieldAlert.setTitle("Information manquante");
            missingFieldAlert.setHeaderText(null);
            missingFieldAlert.setContentText("Veuillez remplir tous les champs requis!");
            missingFieldAlert.showAndWait();
            return; // Return from the method if any required field is empty
        }

        // Retain the existing image URL if no new image is selected
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = selectedEvent.getImage();
        }

        // Convert the input values to appropriate data types
        int idSponso = sp.getSponsorIdByName(tfsponso.getValue());
        int idTerrain = es.getterrainIdByName(tfterrain.getValue());
        String titre = tftitre.getText();
        String description = tfdescription.getText();

        // Timestamp dated = Timestamp.valueOf(tfdd.getValue().atStartOfDay());
        //Timestamp datef = Timestamp.valueOf(tfdf.getValue().atStartOfDay());

        LocalDate startDate = tfdd.getValue();


        LocalTime startTime = LocalTime.parse(tfhd.getText());
        LocalTime endTime = LocalTime.parse(tfhf.getText());


        Timestamp dated = Timestamp.valueOf(LocalDateTime.of(startDate, startTime));
        Timestamp datef = Timestamp.valueOf(LocalDateTime.of(startDate, endTime));


        // Update the selected sponsor object
        selectedEvent.setTitre(tftitre.getText());
        selectedEvent.setDescription(tfdescription.getText());
        selectedEvent.setDatedebut(dated);
        selectedEvent.setDatefin(datef);
        selectedEvent.setIdsponso(idSponso);
        selectedEvent.setIdterrain(idTerrain);
        selectedEvent.setImage(imageUrl);


        // Call the update method in eventService to update the event in the database
        es.update(selectedEvent);

        // Show a success message
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Événement mis à jour");
        successAlert.setHeaderText(null);
        successAlert.setContentText("L'événement a été mis à jour avec succès !");
        successAlert.showAndWait();

        // Clear input fields
        tftitre.clear();
        tfhf.clear();
        tfhd.clear();
        tfdescription.clear();
        tfdd.getEditor().clear();
        tfdf.getEditor().clear();
        tfsponso.setValue(null);
        tfterrain.setValue(null);
        imageimport.setImage(null);

        // Refresh TableView
        refreshTableView();
    }


    @FXML
    void supprimer(ActionEvent event) {
        // Get the selected event from the TableView
        ObservableList<event> selectedEvents = tvevent.getSelectionModel().getSelectedItems();
        if (selectedEvents.isEmpty()) {
            // If no event is selected, show a warning message and return
            Alert noEventSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noEventSelectedAlert.setTitle("Aucun événement sélectionné");
            noEventSelectedAlert.setHeaderText(null);
            noEventSelectedAlert.setContentText("Veuillez sélectionner un ou plusieurs événements à supprimer.");
            noEventSelectedAlert.showAndWait();
            return;
        }

        // Ask for confirmation before deleting the events
        Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDeleteAlert.setTitle("Confirmer la suppression");
        confirmDeleteAlert.setHeaderText("Supprimer les événements sélectionnés");
        confirmDeleteAlert.setContentText("Êtes-vous sûr de vouloir supprimer les événements sélectionnés ?");
        ButtonType confirmButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmDeleteAlert.getButtonTypes().setAll(confirmButton, cancelButton);
        Optional<ButtonType> result = confirmDeleteAlert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            // If the user confirms deletion, delete all selected events
            selectedEvents.forEach(es::delete);
            // Show a success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Événements supprimés");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Les événements sélectionnés ont été supprimés avec succès !");
            successAlert.showAndWait();
            // Refresh TableView
            refreshTableView();
        }
    }

    @FXML
    void initialize() {
        tvevent.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        tvevent.setItems(events);
        coltitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        coldescrition.setCellValueFactory(new PropertyValueFactory<>("description"));
        colimage.setCellValueFactory(new PropertyValueFactory<>("image"));
        coldebut.setCellValueFactory(new PropertyValueFactory<>("datedebut"));
        colfin.setCellValueFactory(new PropertyValueFactory<>("datefin"));
        //colsponso.setCellValueFactory(new PropertyValueFactory<>("idsponso"));
        colsponso.setCellValueFactory(cellData -> {
            int sponsorId = cellData.getValue().getIdsponso();
            String sponsorName = es.getNomsponsoByID(sponsorId);
            System.out.print(sponsorName);
            return new SimpleStringProperty(sponsorName);
        });
        colterrain.setCellValueFactory(cellData -> {
            int terraiId = cellData.getValue().getIdterrain();
            String terrainName = es.getNomterrainIdByID(terraiId);
            System.out.print(terrainName);
            return new SimpleStringProperty(terrainName);
        });
        populateTerrainComboBox();
        populateSponsorComboBox();
        tfhd.setText(""); // or set any default value
        tfhf.setText("");

    }

    Connection connection = DataSource.getInstance().getCnx();
    private ObservableList<String> terrainList = FXCollections.observableArrayList();
    private ObservableList<String> sponsorList = FXCollections.observableArrayList();

    private void populateSponsorComboBox() {
        try {
            String query = "SELECT nom FROM sponso";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String idsponso = resultSet.getString("nom");
                sponsorList.add(idsponso);
            }

            tfsponso.setItems(sponsorList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateTerrainComboBox() {
        try {
            String query = "SELECT nom FROM terrain";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String idterrain = resultSet.getString("nom");
                terrainList.add(idterrain);
            }

            tfterrain.setItems(terrainList);
        } catch (SQLException e) {

            e.printStackTrace();
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

            image = new Image(selectedFile.toURI().toString(), 330, 210, false, true);
            imageimport.setImage(image);
        }
        System.out.println(selectedFile);


    }


    public void slecteddata(javafx.scene.input.MouseEvent mouseEvent) {
        event selectedEvent = tvevent.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            tftitre.setText(selectedEvent.getTitre());
            tfdescription.setText(selectedEvent.getDescription());
            tfdd.setValue(selectedEvent.getDatedebut().toLocalDateTime().toLocalDate());
            // tfdf.setValue(selectedEvent.getDatefin().toLocalDateTime().toLocalDate());
            String imagePath = selectedEvent.getImage();
            tfhd.setText(selectedEvent.getDatedebut().toLocalDateTime().toLocalTime().toString());
            tfhf.setText(selectedEvent.getDatefin().toLocalDateTime().toLocalTime().toString());


            // Retrieve the sponsor name based on the sponsor ID
            String sponsorName = es.getNomsponsoByID(selectedEvent.getIdsponso());
            // Set the sponsor name in the ComboBox
            tfsponso.setValue(sponsorName);

            // Retrieve the terrain name based on the terrain ID
            String terrainName = es.getNomterrainIdByID(selectedEvent.getIdterrain());
            // Set the terrain name in the ComboBox
            tfterrain.setValue(terrainName);
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = new Image(imagePath, 330, 210, false, true);
                imageimport.setImage(image);
            } else {
                // If the image path is empty or null, clear the ImageView
                imageimport.setImage(null);
            }
        }
    }


    @FXML
    void filter(ActionEvent event) {
        events.clear();
        System.out.println("data" + events);

        String searchText = searchTF.getText().toLowerCase();

        events.addAll(es.readAll().stream().filter(events -> {
            // Check if the event title or the sponsor name contains the search text
            String sponsorName = es.getNomsponsoByID(events.getIdsponso());
            String TerrainName = es.getNomterrainIdByID(events.getIdterrain());
            return events.getTitre().toLowerCase().contains(searchText) ||
                    sponsorName.toLowerCase().contains(searchText)||
                    TerrainName.toLowerCase().contains(searchText);
        }).collect(Collectors.toList()));

        System.out.println("data2" + events);


    }

    @FXML
    void calendrier(ActionEvent event) throws IOException {

        loadFXML("/FullCalendar.fxml");

    }

    @FXML
    void pdf(ActionEvent event) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("C://Users//MTIRI LYNDA//Desktop//eventpi//event.pdf"));
            document.open();

            // Title
            Paragraph title = new Paragraph("Liste des Événements\n\n");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            PdfPTable table = new PdfPTable(5); // Nombre de colonnes
            table.setWidthPercentage(100.0F);
            table.getDefaultCell().setPadding(5);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            // En-têtes de colonnes
            String[] headers = {"Titre", "Description", "Date début", "Date fin", "Image"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(new BaseColor(0, 128, 0)); // Green color

                table.addCell(headerCell);
            }

            // Récupérer les données depuis la base de données
            Connection con = DataSource.getInstance().getCnx();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT titre, description, datedebut, datefin, image FROM event");

            // Remplir le tableau avec les données
            while (rs.next()) {
                table.addCell(rs.getString("titre"));
                table.addCell(rs.getString("description"));
                table.addCell(rs.getString("datedebut"));
                table.addCell(rs.getString("datefin"));

                // Ajouter l'image à la cellule
                String imageUrl = rs.getString("image");
                Image img = new Image(imageUrl); // Utilisez le constructeur prenant une URL en paramètre

                // Convertir l'image JavaFX en iText image
                com.itextpdf.text.Image itextImage = com.itextpdf.text.Image.getInstance(img.getUrl());
                itextImage.scaleToFit(100, 100);

                // Ajouter l'image à la cellule
                PdfPCell imageCell = new PdfPCell(itextImage, true);
                imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(imageCell);
            }

            // Ajouter le tableau au document
            document.add(table);

            // Fermer le document
            document.close();

            // Afficher un message de confirmation
            JOptionPane.showMessageDialog(null, "Données exportées en PDF avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du PDF : " + e.getMessage());
            e.printStackTrace();
        }
    }


    ////////Controle de saisie/////
    private boolean verifTitre() {
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(tftitre.getText());
        if (m.find() && m.group().equals(tftitre.getText())) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation des champs");
            alert.setHeaderText(null);
            alert.setContentText("Verifier le titre de l'événement , il ne doit pas contenir des chiffres");
            alert.showAndWait();
            return false;
        }
    }

    private boolean verifDatedebut() {
        Timestamp dated = Timestamp.valueOf(tfdd.getValue().atTime(LocalTime.MIDNIGHT));
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        if (ts.before(dated)) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation des champs");
            alert.setHeaderText(null);
            alert.setContentText("date début inférieur à date système");
            alert.showAndWait();
            return false;
        }}


    private void loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    }



