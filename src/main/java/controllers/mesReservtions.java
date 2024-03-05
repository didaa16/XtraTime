package controllers;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import entities.Reservation;
import services.ServiceReservation;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class mesReservtions  implements Initializable{
    ServiceReservation serviceReservation = new ServiceReservation();
    private Node Node;

    private List<Reservation> getlist() throws SQLException {

        return serviceReservation.afficherParPseudo("dida");
    }
    private Parent parent;
    private Stage stage;
    private Scene scene;
    @FXML
    private Button back;
    @FXML
    private GridPane grid;
    @FXML
    private ScrollPane scrol;
    private List<Reservation> reservations = new ArrayList<>();

    private List<Reservation> getData() throws SQLException {
        List<Reservation> res = getlist();
        return res;
    }


    @FXML
    void back(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grid.getChildren().clear();
        show();
    }

    private void show(){
        grid.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            reservations.addAll(getData());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            for(int i = 0; i< reservations.size(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/card.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                CardController cardController = fxmlLoader.getController();
                cardController.setData(reservations.get(i));

                if (column == 2){
                    column=0;
                    row++;
                }
                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane,new Insets(10));
                grid.setHgap(100);
            }} catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}