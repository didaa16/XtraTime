package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import services.eventService;
import utils.DataSource;
import java.sql.ResultSet;
import javafx.scene.control.Tooltip;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.chart.XYChart;


public class BarChartEvent {


    @FXML
    private BarChart<?, ?> chart;

    @FXML
    private PieChart piechart;

    ObservableList<PieChart.Data> piechartdata;
    @FXML
    private BarChart<String, Number> piechart1;
    eventService es = new eventService();
    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    ArrayList<String> p = new ArrayList<String>();
    ArrayList<Integer> c = new ArrayList<Integer>();

    @FXML
    void initialize()  {
        loadData();
        populateBarChart();

        piechart.setData(piechartdata);
    }

    public void loadData() {

        String query = "SELECT COUNT(*) AS count, idsponso FROM event GROUP BY idsponso";
        piechartdata = FXCollections.observableArrayList();

        Connection con = DataSource.getInstance().getCnx();

        try {
            ResultSet rs = con.createStatement().executeQuery(query);
            while (rs.next()) {
                int count = rs.getInt("count");
                int idsponso = rs.getInt("idsponso");

                // Récupérer le nom du sponsor par son ID
                String sponsorName = es.getNomsponsoByID(idsponso);

                // Créer le PieChart.Data avec le nom du sponsor et le nombre d'événements
                PieChart.Data data = new PieChart.Data(sponsorName, count);
                piechartdata.add(data);
            }

            // Créer un Tooltip pour afficher le nombre d'événements
            Tooltip tooltip = new Tooltip();
            Tooltip.install(piechart, tooltip); // Attacher le Tooltip au PieChart

            // Ajouter un EventHandler pour le survol du PieChart
            piechart.setOnMouseMoved(event -> {
                // Récupérer le nœud graphique sous la souris
                Node node = event.getPickResult().getIntersectedNode();
                if (node instanceof Text) {
                    // Si le nœud est un texte (représentant le nom du sponsor), mettre à jour le Tooltip avec le nombre d'événements
                    String sponsorName = ((Text) node).getText();
                    for (PieChart.Data data : piechart.getData()) {
                        if (data.getName().equals(sponsorName)) {
                            tooltip.setText("Nombre d'événements : " + (int) data.getPieValue());
                            tooltip.setStyle("-fx-text-fill: white;"); // Définir la couleur du texte en blanc
                            Tooltip.install(piechart, tooltip);
                            return;
                        }
                    }
                }
                // Si aucun texte n'est survolé, effacer le Tooltip
                tooltip.setText("");
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void populateBarChart() {

        CategoryAxis xAxis = new CategoryAxis(); // Axe X pour les mois
        NumberAxis yAxis = new NumberAxis(); // Axe Y pour le nombre d'événements
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        // Définissez les noms des mois dans l'ordre
        String[] months = {"Janvier", "February", "Mars", "Avril", "May", "June", "July", "August", "September", "October", "November", "December"};

        // Créez une série de données pour le BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Exécutez la requête SQL pour obtenir le nombre d'événements par mois

        String query = "SELECT MONTH(datedebut) AS month, COUNT(*) AS event_count FROM event GROUP BY MONTH(datedebut)";
        Connection con = DataSource.getInstance().getCnx();
        try (
             PreparedStatement preparedStatement = con.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int monthIndex = resultSet.getInt("month");
                int eventCount = resultSet.getInt("event_count");

                // Ajoutez le nombre d'événements à la série de données sous le nom du mois correspondant
                series.getData().add(new XYChart.Data<>(months[monthIndex - 1], eventCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ajoutez la série de données au BarChart
        piechart1.getData().add(series); // Utilisation de la référence injectée au lieu de créer un nouveau BarChart
    }
           }

