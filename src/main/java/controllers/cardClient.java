package controllers;

import entities.Img;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import services.ServiceImg;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class cardClient {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label ageTF;

    @FXML
    private Label cinTF;

    @FXML
    private Label emailTF;

    @FXML
    private Label nomEtprenomTF;

    @FXML
    private Label numTelTF;

    @FXML
    private Circle pdp;

    @FXML
    private Label pseudoTF;
    private static Img loggedInImage;
    private static Utilisateur loggedInUser;
    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }
    ServiceImg serviceImg = new ServiceImg();

    @FXML
    void initialize() {
        pseudoTF.setStyle("-fx-text-fill: white;");
        cinTF.setStyle("-fx-text-fill: white;");
        nomEtprenomTF.setStyle("-fx-text-fill: white;");
        numTelTF.setStyle("-fx-text-fill: white;");
        emailTF.setStyle("-fx-text-fill: white;");
        ageTF.setStyle("-fx-text-fill: white;");
        if (loggedInUser!=null) {
            try {
                loggedInImage = serviceImg.afficherImageParPseudo(loggedInUser.getPseudo());
                pseudoTF.setText(loggedInUser.getPseudo());
                cinTF.setText(String.valueOf(loggedInUser.getCin()));
                nomEtprenomTF.setText(loggedInUser.getNom() + " " + loggedInUser.getPrenom());
                ageTF.setText(String.valueOf(loggedInUser.getAge()));
                numTelTF.setText(String.valueOf(loggedInUser.getNumtel()));
                emailTF.setText(loggedInUser.getEmail());
                if (serviceImg.imgExiste(loggedInUser.getPseudo())) {
                    String absolutePath = loggedInImage.getImg();
                    Image image = new Image(new File(absolutePath).toURI().toString());
                    pdp.setFill(new ImagePattern(image));
                } else {
                    String absolutePath = "/Design/pdpVide.jpg";
                    Image image = new Image(getClass().getResourceAsStream(absolutePath));
                    pdp.setFill(new ImagePattern(image));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
