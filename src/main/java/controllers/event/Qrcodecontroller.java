package controllers.event;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import entities.event.event;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Qrcodecontroller {

    private event ev = new event();

    public void initialize(URL url, ResourceBundle rb) throws SQLException {
        System.out.println(".....");

        this.ini(ev);
    }

    @FXML
    public void ini(event ev) throws SQLException{
      /*
        ConsulterCoursActiviteController c=new ConsulterCoursActiviteController();
        CoursActivite ca;
                ca=c.envCA();*/

        String myWeb = "Nom de l'event= " + ev.getTitre()+ " , = "+ev.getDescription();
        System.out.println(myWeb);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        int width = 300;
        int height = 300;
        String fileType = "png";

        BufferedImage bufferedImage = null;
        try {
            BitMatrix byteMatrix = qrCodeWriter.encode(myWeb, BarcodeFormat.QR_CODE, width, height);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();

            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            System.out.println("Success...");

        } catch (WriterException ex) {
            Logger.getLogger(Qrcodecontroller.class.getName()).log(Level.SEVERE, null, ex);
        }

        ImageView qrView = new ImageView();
        qrView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));

        StackPane root = new StackPane();
        root.getChildren().add(qrView);
        Scene scene = new Scene(root, 350, 350);
        Stage primaryStage=new Stage();
        primaryStage.setTitle("détail evenment");
        primaryStage.setScene(scene);
        primaryStage.show();


    }


}
