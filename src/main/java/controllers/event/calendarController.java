package controllers.event;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class calendarController implements Initializable {

    @FXML
    public Pane calendarPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
// Créez une instance de FullCalendarView avec l'année et le mois actuels
        FullCalendarView calendarView = new FullCalendarView(YearMonth.now());

        // Obtenez la vue du calendrier et ajoutez-la à votre Pane
        calendarPane.getChildren().add(calendarView.getView());
    }    
    
}
