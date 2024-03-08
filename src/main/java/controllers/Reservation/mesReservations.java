package controllers.Reservation;

import entities.Reservation.Reservation;
import entities.utilisateur.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import services.Reservation.ServiceReservation;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class mesReservations implements Initializable {
    private static Utilisateur loggedInUser;
    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }
    ServiceReservation serviceReservation = new ServiceReservation();
    CardController cardController = new CardController();
    private Node Node;

    private List<Reservation> getlist() throws SQLException {
        return serviceReservation.afficherParPseudo(loggedInUser.getPseudo());
    }

    private Map<String, List<Reservation>> getData() throws SQLException {
        Map<String, List<Reservation>> reservationsByDate = new HashMap<>();
        List<Reservation> reservations = getlist();
        for (Reservation reservation : reservations) {
            String date = reservation.getDate(); // Supposons que la date est stockée sous forme de chaîne de caractères
            if (!reservationsByDate.containsKey(date)) {
                reservationsByDate.put(date, new ArrayList<>());
            }
            reservationsByDate.get(date).add(reservation);
        }
        return reservationsByDate;
    }

    @FXML
    private Button back;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scrol;

    private Map<String, List<Reservation>> reservationsByDate = new HashMap<>();

    @FXML
    void back(ActionEvent event) {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grid.getChildren().clear();
        try {
            reservationsByDate = getData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        show();
    }

    private void show() {
        grid.getChildren().clear();
        int column = 0;
        int row = 1;
        for (Map.Entry<String, List<Reservation>> entry : reservationsByDate.entrySet()) {
            String date = entry.getKey();
            List<Reservation> reservations = entry.getValue();
            for (Reservation reservation : reservations) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/Reservation/card.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();

                    CardController cardController = fxmlLoader.getController();
                    cardController.setData(reservation); // Passer chaque réservation individuellement

                    if (column == 2) {
                        column = 0;
                        row++;
                    }
                    grid.add(anchorPane, column++, row);
                    GridPane.setMargin(anchorPane, new Insets(10));
                    grid.setHgap(100);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
