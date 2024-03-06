package controllers.local;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.fxml.Initializable;

import entities.local.complexe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.local.ServiceComplexe;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;
import javafx.util.Duration;
import javafx.scene.control.Label;

public class InscriptionController implements Initializable {
    private Image image;
  ServiceComplexe ServiceComplexe = new ServiceComplexe();

    @FXML
    private TextField nom;
private  Label label;
    @FXML
    private WebView mapWebView;
    @FXML
    private TextField adr;

    @FXML
    private TextField tel;

    @FXML
    private ImageView pt;

    @FXML
    private ImageView im;
    @FXML
    private Button up;
    private String imagePath;
    private String imagePath1;
    @FXML
    private Button upp;

    @FXML
    void afficher(ActionEvent event) {
        try {
            Parent root= FXMLLoader.load(getClass().getResource("/FxmlLocal/AfficherComplexe.fxml"));
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Utiliser une expression régulière pour valider le numéro de téléphone
        // Par exemple, la regex suivante vérifie si le numéro a 10 chiffres et commence par un chiffre entre 0 et 9
        return Pattern.matches("\\d{8}", phoneNumber);
    }
    private static boolean isValidNom(String nom) {
        // Utiliser une expression régulière pour vérifier si la chaîne de caractères ne contient que des lettres
        // La regex ^[a-zA-Z]+$ vérifie que la chaîne contient uniquement des lettres (minuscules ou majuscules)
        return Pattern.matches("^[a-zA-Z]+$", nom);
    }
    /*   private static boolean isValidAdresse(String adresse) {
          // Utiliser une expression régulière pour vérifier si la chaîne de caractères de l'adresse est valide
          // La regex ^[a-zA-Z0-9\s.,'-]+$ vérifie que la chaîne contient uniquement des lettres, chiffres, espaces et caractères spéciaux couramment utilisés dans les adresses
          return Pattern.matches("^[a-zA-Z0-9\\s.,'-]+$", adresse);
   }
    /*  private void showNotification(String message) {
          message="succes ";
          Notifications.create()
                  .title("Notification")
                  .text(message)
                  .hideAfter(Duration.seconds(5)) // Masquer la notification après 5 secondes
                  .show();
      }*/
    @FXML
    void addcomplexe(ActionEvent event) {
        String nomC = nom.getText();
        String idlC = "dida16";
        String adrC = adr.getText();
        String telC = tel.getText();
        if (nomC.isEmpty() || idlC.isEmpty() || adrC.isEmpty() || telC.isEmpty() || imagePath.isEmpty() || imagePath1.isEmpty()) {
            showAlert("Erreur", "Champs vides", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }

        // Vérifier que le numéro de téléphone est valide
        if (!isValidPhoneNumber(tel.getText())) {
            showAlert("Erreur", "Numéro de téléphone invalide", "Veuillez saisir un numéro de téléphone valide.", Alert.AlertType.ERROR);
            return;
        }
        if (!isValidNom(nom.getText())) {
            showAlert("Erreur", "Nom invalide", "Veuillez saisir un nom valide.", Alert.AlertType.ERROR);
            return;
        }
      /*  if (!isValidAdresse(adr.getText())) {
            showAlert("Erreur", "adresse invalide", "Veuillez saisir une adresse valide.", Alert.AlertType.ERROR);
            return;
        }
*/
        try {
            ServiceComplexe.ajouter(new complexe(nomC,idlC, adrC, telC,  imagePath, imagePath1));
//showNotification("ajout complexe avec succes");
           boolean  oeuvreAdded = true;

            if (oeuvreAdded) {
                Notifications.create()
                        .title("Notification Title")
                        .text("COMPLEXE AJOUTEE")
                        .hideAfter(Duration.seconds(9))
                        .showInformation();
            }
        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("succes");
        alert.setContentText("personne ajoute");
        alert.showAndWait(); }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     *
     */



    @FXML
    void addterrain(ActionEvent event) {
        try {
            Parent root= FXMLLoader.load(getClass().getResource("/FxmlLocal/AddTerrain.fxml"));
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Event listener for upload button
            up.setOnAction((ActionEvent event) -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choisir une image");
               // fileChooser.getExtensionFilters().addAll(
                   //     new ExtensionFilter("Image Files", ".png", ".jpg", "*.gif"));
                File selectedFile = fileChooser.showOpenDialog(new Stage());
                if (selectedFile != null) {
                    imagePath = selectedFile.getAbsolutePath();
                    Image image = new Image(selectedFile.toURI().toString());
                    pt.setImage(image);
                }
            });
        upp.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
          //  fileChooser.getExtensionFilters().addAll(
              //      new ExtensionFilter("", ".png", ".jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                imagePath1 = selectedFile.getAbsolutePath();
                Image image = new Image(selectedFile.toURI().toString());
                im.setImage(image);
            }
        });
        WebEngine webEngine = mapWebView.getEngine();
        webEngine.load(getClass().getResource("/FxmlLocal/Maps.html").toExternalForm());
        }
    @FXML
    private void loadMap( String governorate, String city) {
        WebView mapWebView;
        mapWebView = null;
        WebEngine webEngine = mapWebView.getEngine();
        String htmlContent = generateMapHtml( governorate, city);
        webEngine.loadContent(htmlContent);

    }

    private String generateMapHtml( String governorate, String city) {
        String mapUrl = "https://maps.google.com/maps?q=" +

                governorate.replace(" ", "%20") + ",%20" +
                city.replace(" ", "%20") + "&t=k&z=16&output=embed";
        return mapUrl;
    }
    private void loadMap(String localisation) {
        WebEngine webEngine = mapWebView.getEngine();

        // Generate HTML content with the correct map URL
        String htmlContent = generateMapHtml(String.valueOf(adr));

        // Load the HTML content into the WebView
        webEngine.loadContent(htmlContent);
    }

    private String generateMapHtml(String localisation) {
        // Construct the map URL based on the club name, governorate, and city
        String mapUrl = "https://maps.google.com/maps?q=" +

                localisation.replace(" ", "%20") + "&t=k&z=16&output=embed";

        // Generate HTML content with the correct map URL
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Google Maps Example</title>\n" +
                "    <style>\n" +
                "        /* Adjust the size and position of the map */\n" +
                "        #mapouter {\n" +
                "            position: relative;\n" +
                "            text-align: right;\n" +
                "            height: 500px; /* Adjust the height as needed */\n" +
                "            width: 500px; /* Adjust the width as needed */\n" +
                "        }\n" +
                "\n" +
                "        #gmap_canvas2 {\n" +
                "            overflow: hidden;\n" +
                "            background: none !important;\n" +
                "            height: 500px; /* Adjust the height as needed */\n" +
                "            width: 500px; /* Adjust the width as needed */\n" +
                "        }\n" +
                "\n" +
                "        #gmap_canvas {\n" +
                "            width: 100%;\n" +
                "            height: 100%;\n" +
                "            border: 0;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"mapouter\">\n" +
                "    <div id=\"gmap_canvas2\">\n" +
                "        <iframe id=\"gmap_canvas\"\n" +
                "                src=\"" + mapUrl + "\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"></iframe>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }



}
