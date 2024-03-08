package controllers.Reservation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TimelineApiForecastSample {
    private static String token = "2P9SLYCPDQ75RZ8AREQY5QSJP";
    private static String country = "Tunisia";

    public static void setCountry(String country) {
        TimelineApiForecastSample.country = country;
    }

    public static void downloadWeatherExcel(String date, Stage primaryStage) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+country+"/"+date+"/"+date+"?unitGroup=metric&include=days&key=2P9SLYCPDQ75RZ8AREQY5QSJP&contentType=xlsx"))
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        byte[] responseBody = response.body();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Weather Forecast Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        // Show save file dialog
        Path filePath = FileSystems.getDefault().getPath(fileChooser.showSaveDialog(primaryStage).getAbsolutePath());

        try (FileOutputStream fos = new FileOutputStream(filePath.toString())) {
            fos.write(responseBody);
            System.out.println("Excel file saved successfully at: " + filePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
