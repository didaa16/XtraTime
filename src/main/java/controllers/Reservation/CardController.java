package controllers.Reservation;

import java.net.URL;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import entities.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import services.ServiceReservation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;


public class CardController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label affDate;

    @FXML
    private Label affDuree;

    @FXML
    private Label affEquipements;

    @FXML
    private Label affPrix;

    @FXML
    private Label affTerrainId;

    private Reservation reservationE;
    ServiceReservation ser = new ServiceReservation();

    public void setReservationE(Reservation res) {
        reservationE = res;
    }

    @FXML
    void initialize() {
    }

    public void setData(Reservation reservation) throws SQLException {
        ServiceReservation serviceReservation = new ServiceReservation();
        List<Reservation> reservations = serviceReservation.afficher();

        if (!reservations.isEmpty()) {
            this.reservationE = reservation;
            affDate.setText(reservation.getDate());
            affDuree.setText(reservation.getDuree());
            affPrix.setText(String.valueOf(reservation.getPrix()));
            affTerrainId.setText(String.valueOf(reservation.getTerrainId()));
            affEquipements.setText(reservation.getEquipements());
        }
    }
    @FXML
    void modifierResOnClick(ActionEvent actionEvent) {
        ModifierReservationController.setReservation(reservationE);
        ser.changeScreen(actionEvent, "/modifierReservation.fxml", "XTRATIME");
    }


    public void voirTemperatureOnClick(ActionEvent actionEvent) {
        try {
            String date = reservationE.getDate(); // Assuming this gets the date from somewhere
            TimelineApiForecastSample.setCountry("Tunisia"); // Set country dynamically if needed
            TimelineApiForecastSample.downloadWeatherExcel(date, (Stage) affDate.getScene().getWindow());
            System.out.println("Weather Excel file downloaded for date: " + date);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


}

