package controllers.event;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.event.DataSource;

public class loginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button blogin;

    @FXML
    private TextField tfpass;

    @FXML
    private TextField tfpseudo;

    @FXML
    void login(ActionEvent event) {
        Connection conn = DataSource.getInstance().getCnx();
        ResultSet rs = null;
        PreparedStatement pst = null;

        try {
            pst = conn.prepareStatement("SELECT * FROM utilisateurs WHERE pseudo=? AND mdp=?");
            pst.setString(1, tfpseudo.getText());
            pst.setString(2, tfpass.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                Stage loginStage = (Stage) blogin.getScene().getWindow();
                loginStage.close();

                if ("Admin".equals(role)) {
                    loadFXML("/fxmlevent/base.fxml");
                } else {
                    loadFXML("/fxmlevent/baseFront.fxml");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Login error", ButtonType.OK);
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }


        @FXML
    void initialize() {
        assert blogin != null : "fx:id=\"blogin\" was not injected: check your FXML file 'login.fxml'.";
        assert tfpass != null : "fx:id=\"tfpass\" was not injected: check your FXML file 'login.fxml'.";
        assert tfpseudo != null : "fx:id=\"tfpseudo\" was not injected: check your FXML file 'login.fxml'.";

    }


}
