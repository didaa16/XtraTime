package Controllers;

import entities.Abonnement;
import entities.Pack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import services.ServiceAbonnement;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HomeStatsController implements Initializable {
    @javafx.fxml.FXML
    private Button AB;
    @javafx.fxml.FXML
    private Button AFF;
    @javafx.fxml.FXML
    private StackPane chartContainer;
    @javafx.fxml.FXML
    private Button PK;


    private ChartPanel weightChartPanel;
    ServiceAbonnement sa = new ServiceAbonnement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Abonnement> abonnements = null;
        try {
            abonnements = sa.getAllAbonnements();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Map<Integer, Long> numberPacks = abonnements.stream()
                .collect(Collectors.groupingBy(Abonnement::getPackId, Collectors.counting()));

        // Calculate total number of abonnements
        long totalAbonnements = abonnements.size();

        // Create dataset for pie chart
        PieChart.Data[] pieChartData = numberPacks.entrySet().stream()
                .map(entry -> {
                    double percentage = entry.getValue() / (double) totalAbonnements * 100;
                    return new PieChart.Data("Pack " + entry.getKey() + " (" + String.format("%.2f", percentage) + "%)", percentage);
                })
                .toArray(PieChart.Data[]::new);

        // Create the pie chart
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(pieChartData);

        // Customize the labels to display percentage and set color to white
        for (PieChart.Data data : pieChartData) {
            data.getNode().setStyle("-fx-text-fill: white;");
        }

        // Add the pie chart to the chart container
        chartContainer.getChildren().add(pieChart);
    }

    @FXML
    void changeA(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherA.fxml"));
            AB.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void changeP(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ajout.fxml"));
            AB.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
