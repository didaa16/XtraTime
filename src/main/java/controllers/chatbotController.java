package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import okhttp3.*;
import utils.DataSource;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class chatbotController {

    @FXML
    private TextField inputTextField;

    @FXML
    private TextArea outputLabel;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    @FXML
    private HBox additionalButtonsContainer;

    private boolean chatGPTEnabled = false;
java.sql.Connection connection = DataSource.getInstance().getCnx();
    @FXML
    private void initialize() {
        // Masquer les boutons "Oui" et "Non" au chargement de la scène
        yesButton.setVisible(false);
        noButton.setVisible(false);
    }

    @FXML
    private void processInput(ActionEvent event) {
        String input = inputTextField.getText().toLowerCase();
        if (input.equalsIgnoreCase("Bonjour")) {
            outputLabel.setText("Est-ce que tu veux des informations concernant les événements ou tu veux utiliser chatgpt?");
            chatGPTEnabled = true;
            yesButton.setVisible(true);
            noButton.setVisible(true);
        } else if (chatGPTEnabled) {
            CompletableFuture.supplyAsync(() -> getOpenAIAPIAnswer(input))
                    .thenAccept(answer -> Platform.runLater(() -> outputLabel.setText(answer)));
        } else {
            outputLabel.setText("Je ne comprends pas. Tapez 'Bonjour' pour commencer la conversation.");
        }
        inputTextField.clear();
    }

    private static final String apiKey = "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";
    private static final String model = "gpt-3.5-turbo";  // Adjust the model name

    private static String getOpenAIAPIAnswer(String question) {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            String requestBody = "{\"model\":\"" + model + "\",\"messages\":[{\"role\":\"system\",\"content\":\"You are a helpful assistant.\"},{\"role\":\"user\",\"content\":\"" + question + "\"}],\"max_tokens\":150}";

            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")  // Use the correct endpoint for chat
                    .post(RequestBody.create(requestBody, mediaType))
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                // Parse JSON response and extract content
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonArray choicesArray = jsonResponse.getAsJsonArray("choices");
                if (choicesArray.size() > 0) {
                    JsonObject firstChoice = choicesArray.get(0).getAsJsonObject();
                    JsonObject message = firstChoice.getAsJsonObject("message");
                    String content = message.getAsJsonPrimitive("content").getAsString();
                    return content;
                } else {
                    return "No response content available.";
                }
            } else {
                // Log detailed error information
                System.err.println("Error Response Code: " + response.code());
                System.err.println("Error Response Body: " + responseBody);
                return "Oops! An error occurred while fetching the answer. Response code: " + response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Oops! An error occurred while processing your request. Error: " + e.getMessage();
        }
    }

    @FXML
    private void handleYes(ActionEvent event) {
        if (chatGPTEnabled) {
            outputLabel.setText("Posez votre question.");
            inputTextField.setDisable(false);
            yesButton.setVisible(false);
            noButton.setVisible(false);
        }
    }

    @FXML
    private void handleNo(ActionEvent event) {
        chatGPTEnabled = false;
        outputLabel.setText("Vous pouvez choisir quels evennements vous voulez parcourir EVENNEMENT.");
        inputTextField.setDisable(false);
        yesButton.setVisible(false);
        noButton.setVisible(false);

        // Afficher les trois boutons supplémentaires
        additionalButtonsContainer.setVisible(true);
    }

    @FXML
    private void handleButton1(ActionEvent event) {
        chatGPTEnabled = false;
        try {



            // Création de la requête SQL pour récupérer les titres des événements
            String requete = "SELECT titre FROM event WHERE DATE(datedebut) < CURDATE()";

            // Préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(requete);


            // Exécution de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            // Traitement des résultats
            StringBuilder resultat = new StringBuilder();
            while (resultSet.next()) {
                resultat.append(resultSet.getString("titre")).append("\n");
            }

            // Affichage des résultats dans outputLabel
            outputLabel.setText(resultat.toString());

            // Fermeture des ressources


        } catch (SQLException e) {
            e.printStackTrace();
            outputLabel.setText("Une erreur s'est produite lors de la récupération des événements.");
        }
    }

    @FXML
    private void handleButton2(ActionEvent event) {
        // Action pour le bouton
        chatGPTEnabled = false;
        try {



            // Création de la requête SQL pour récupérer les titres des événements
            String requete = "SELECT titre FROM event WHERE DATE(datedebut) = CURDATE()";

            // Préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(requete);


            // Exécution de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            // Traitement des résultats
            StringBuilder resultat = new StringBuilder();
            while (resultSet.next()) {
                resultat.append(resultSet.getString("titre")).append("\n");
            }

            // Affichage des résultats dans outputLabel
            outputLabel.setText(resultat.toString());

            // Fermeture des ressources


        } catch (SQLException e) {
            e.printStackTrace();
            outputLabel.setText("Une erreur s'est produite lors de la récupération des événements.");
        }
    }

    @FXML
    private void handleButton3(ActionEvent event) {
        chatGPTEnabled = false;
        try {



            // Création de la requête SQL pour récupérer les titres des événements
            String requete = "SELECT titre FROM event WHERE DATE(datedebut) > CURDATE()";

            // Préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(requete);


            // Exécution de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            // Traitement des résultats
            StringBuilder resultat = new StringBuilder();
            while (resultSet.next()) {
                resultat.append(resultSet.getString("titre")).append("\n");
            }

            // Affichage des résultats dans outputLabel
            outputLabel.setText(resultat.toString());

            // Fermeture des ressources


        } catch (SQLException e) {
            e.printStackTrace();
            outputLabel.setText("Une erreur s'est produite lors de la récupération des événements.");
        }
    }
}
