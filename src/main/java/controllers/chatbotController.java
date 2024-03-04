package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;


import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.*;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import okhttp3.*;

import java.io.IOException;
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
    private void processInput(ActionEvent event) {
        String input = inputTextField.getText();
        CompletableFuture.supplyAsync(() -> getOpenAIAPIAnswer(input))
                .thenAccept(answer -> Platform.runLater(() -> {
                    outputLabel.setText(answer);
                    handleButtonVisibility(input);
                }));
        inputTextField.clear();
    }

    private static final String apiKey = "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";
    private static final String model = "gpt-3.5-turbo";  // Adjust the model name

    private static String getOpenAIAPIAnswer(String question) {
        if (question.equalsIgnoreCase("")) {
            return "Est-ce que tu veux des informations concernant les événements ou tu veux utiliser chatgpt?";
        } else {
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
    }

    private void handleButtonVisibility(String input) {
        if (input.equalsIgnoreCase("Bonjour")) {
            yesButton.setVisible(true);
            noButton.setVisible(true);
        } else {
            yesButton.setVisible(false);
            noButton.setVisible(false);
        }
    }

    @FXML
    private void handleYes(ActionEvent event) {
        System.out.println("oui");
    }

    @FXML
    private void handleNo(ActionEvent event) {
        // Ajoutez votre logique ici pour gérer le clic sur le bouton "Non"
        System.out.println("non");
    }


}