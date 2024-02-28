package controllers;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import entities.Reservation;
import entities.Terrain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import services.ServiceEquipement;
import services.ServiceTerrain;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class affichageTerrain {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private VBox terrainCard;
    Reservations reservationsController;
    private Text idText;
    @FXML
    private ServiceTerrain sterrain = new ServiceTerrain();
    private static int idTerrain;

    @FXML
    void initialize() {
        terrainCard.setOnMouseClicked(event -> {
            navigateToReservationInterface();
        });
//
        try {
            List<Terrain> terrains = sterrain.afficher();
            for (Terrain terrain : terrains) {
                System.out.println(idTerrain);

                // Create a VBox for each terrain
                VBox terrainInfo = new VBox();
                terrainInfo.setPrefWidth(200); // Set preferred width
                // Add details of the terrain (replace with actual data from Terrain object)
                terrainInfo.getChildren().addAll(
                        new Text("Terrain ID: " + terrain.getId()),
                        new Text("Terrain Name: " + terrain.getNom()),
                        new Text("Capacité: " + terrain.getCapacite()),
                        new Text("Type: " + terrain.getType()),
                        new Text("Disponibilité: " + terrain.getDisp()),
                        new Text("Prix: $" + terrain.getPrix())
                );

                // Add the VBox containing terrain details to the main VBox
                terrainCard.getChildren().add(terrainInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }
    @FXML
    private void navigateToReservationInterface() {
        System.out.println("Navigating to the 'reservation' interface...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservation.fxml"));
            Parent root = loader.load();
            Reservations reservationsController = loader.getController();

            // Set the terrain ID in the Reservations controller
            reservationsController.setTerrainId();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the stage (window) from the VBox and set the scene
            Stage stage = (Stage) terrainCard.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Handle any potential errors loading the FXML file
            System.out.println(e.getMessage());
        }
    }


    public void navigateToMesReservationsInterface(ActionEvent actionEvent) {

    }
}
