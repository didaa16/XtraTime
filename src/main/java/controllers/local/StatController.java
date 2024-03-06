// StatController.java
package controllers.local;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import services.local.ServiceTerrain;
import entities.local.terrain;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatController {
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Button back;
    private ServiceTerrain serviceTerrain;

    public StatController() {
        serviceTerrain = new ServiceTerrain();
    }
    @FXML
    void back(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FxmlLocal/AddTerrain.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    // Méthode appelée lorsque l'interface utilisateur est initialisée
    @FXML
    public void initialize() {
        try {
            // Récupérer la liste des terrains depuis le service
            List<terrain> terrains = serviceTerrain.afficher();

            // Collecter les statistiques basées sur le champ "type"
            Map<String, Integer> statsByType = collectStatsByType(terrains);

            // Créer une série de données pour chaque type de terrain
            ObservableList<BarChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
            statsByType.forEach((type, count) -> {
                BarChart.Series<String, Number> series = new BarChart.Series<>();
                series.getData().add(new BarChart.Data<>(type, count));
                barChartData.add(series);
            });

            // Afficher les données sur le graphique
            barChart.setData(barChartData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour collecter les statistiques basées sur le champ "type"
    private Map<String, Integer> collectStatsByType(List<terrain> terrains) {
        Map<String, Integer> statsByType = new HashMap<>();

        for (terrain terrain : terrains) {
            String type = terrain.getType();
            statsByType.put(type, statsByType.getOrDefault(type, 0) + 1);
        }

        return statsByType;
    }

    // Méthode appelée en réponse à une action de l'utilisateur
    @FXML
    public void onButtonClicked() {
        // Par exemple, réagir au clic d'un bouton
        System.out.println("Bouton cliqué !");
    }
}
