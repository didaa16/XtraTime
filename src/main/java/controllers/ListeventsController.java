package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import entities.event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.eventService;
import utils.DataSource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.scene.input.MouseEvent;

import javax.xml.ws.Holder;





public class ListeventsController {






    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Button bouton;
    public Holder<String> holdID = new Holder();
    @FXML
    private AnchorPane Paneeventsfx;
    @FXML
    private AnchorPane Paneeventsfx1;
    @FXML
    private AnchorPane Paneeventsfx11;

    @FXML
    private ImageView imageeventspanefx;

    @FXML
    private ImageView imageeventspanefx1;
    @FXML
    private ImageView imageeventspanefx11;
    @FXML
    private Label subjecteventspanefx;

    @FXML
    private Label subjecteventspanefx1;

    @FXML
    private Label subjecteventspanefx11;
    @FXML
    private Label subjecteventspanefx2;

    @FXML
    private Label subjecteventspanefx22;

    @FXML
    private Label subjecteventspanefx222;

    @FXML
    private Label heureeventspanefx;

    @FXML
    private Label heureeventspanefx1;

    @FXML
    private Label heureeventspanefx11;

    @FXML
    private Label label;

    @FXML
    private Label label1;

    @FXML
    private Label label11;

    @FXML
    private AnchorPane liste_events;
    public ArrayList<ImageView> ListImagese;
    public ArrayList<ImageView> ListImageseQrcode;
    public ArrayList<Label> Listlabeltitleevent;
    public ArrayList<Label> Listlabeldateevent;
    public ArrayList<Label> Listlabelheurevent;
    public ArrayList<Label> Listlabelchrono;
    @FXML
    private ImageView qrcode;

    @FXML
    private ImageView qrcode1;

    @FXML
    private ImageView qrcode11;
    private eventService eventService;

    private ObservableList<event> eventData;
    int i = 0;
    int CurrentEvent = 0;
    private ObservableList<event> data;
    eventService es = new eventService();
    public ArrayList<AnchorPane> Listpaneevent;
    Connection con = null;
    public  static Holder<String> holdIDs = new Holder();
    public ObservableList<event> dataa = FXCollections.observableArrayList();


    public ListeventsController() {
        this.ListImagese = new ArrayList();
        this.Listpaneevent = new ArrayList();
        this.Listlabeltitleevent = new ArrayList();
        this.Listlabeldateevent= new ArrayList();
        this.Listlabelheurevent= new ArrayList();
        this.ListImageseQrcode = new ArrayList();
        this.Listlabelchrono = new ArrayList();
    }


    @FXML
    void initialize() throws SQLException {

        this.data = FXCollections.observableArrayList();
        this.con = DataSource.getInstance().getCnx();
        this.data.addAll(this.es.readAll());

        try {
            this.getUserData(this.CurrentEvent);

        } catch (SQLException var4) {
            var4.printStackTrace();
        }


    }



    public void getUserData(int CurrentEvent) throws SQLException {
        this.Listpaneevent.add(this.Paneeventsfx);
        this.Listpaneevent.add(this.Paneeventsfx1);
        this.Listpaneevent.add(this.Paneeventsfx11);
        //image
        this.ListImagese.add(this.imageeventspanefx);
        this.ListImagese.add(this.imageeventspanefx1);
        this.ListImagese.add(this.imageeventspanefx11);
        //titre
        this.Listlabeltitleevent.add(this.subjecteventspanefx);
        this.Listlabeltitleevent.add(this.subjecteventspanefx1);
        this.Listlabeltitleevent.add(this.subjecteventspanefx11);
        //date
        this.Listlabeldateevent.add(this.subjecteventspanefx2);
        this.Listlabeldateevent.add(this.subjecteventspanefx22);
        this.Listlabeldateevent.add(this.subjecteventspanefx222);
        //time-heure
        this.Listlabelheurevent.add(this.heureeventspanefx);
        this.Listlabelheurevent.add(this.heureeventspanefx1);
        this.Listlabelheurevent.add(this.heureeventspanefx11);

        this.ListImageseQrcode.add(this.qrcode);
        this.ListImageseQrcode.add(this.qrcode1);
        this.ListImageseQrcode.add(this.qrcode11);

        this.Listlabelchrono.add(this.label);
        this.Listlabelchrono.add(this.label1);
        this.Listlabelchrono.add(this.label11);


        int Nombre = this.es.numberevent();
        for (this.i = CurrentEvent; this.i < CurrentEvent + 3; ++this.i) {
            System.out.println(((event) this.data.get(this.i)).getImage());
            Image image = new Image(((event) this.data.get(this.i)).getImage());
            ((ImageView) this.ListImagese.get(this.i)).setImage(image);
            ((Label)this.Listlabeltitleevent.get(this.i)).setText(((event)this.data.get(this.i)).getTitre());
            // Get the timestamp
            Timestamp timestamp = ((event) this.data.get(this.i)).getDatedebut();

            // Convert the timestamp to LocalDateTime
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            // Format date and time
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            // Set date to subjecteventspanefx2, subjecteventspanefx22, subjecteventspanefx222
            ((Label) this.Listlabeldateevent.get(this.i)).setText(dateTime.toLocalDate().format(dateFormatter));

           // Set time (hour) to heureeventspanefx, heureeventspanefx1, heureeventspanefx11
            ((Label) this.Listlabelheurevent.get(this.i)).setText(dateTime.toLocalTime().format(timeFormatter));
            ImageView qrImageView = this.ListImageseQrcode.get(this.i);
            event ev = this.data.get(this.i);

            // Call ini method passing the event and its corresponding qrImageView
            ini(ev, qrImageView);
            //Image imageqr = new Image(((event) this.data.get(q.ini(e));
            //((ImageView) this.ListImageseQrcode.get(this.i)).setImage(imageqr);

            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime eventTime = timestamp.toLocalDateTime();
            Duration duration = Duration.between(currentTime, eventTime);

            // Vérifier si l'événement est à venir
            if (duration.isNegative() || duration.isZero()) {
                // L'événement est passé ou en cours, donc aucun compte à rebours n'est nécessaire
                ((Label) this.Listlabelchrono.get(this.i)).setText("Événement passé ou en cours");
            } else {
                // Afficher le temps restant sous forme de compte à rebours
                long days = duration.toDays();
                long hours = duration.toHours() % 24;
                long minutes = duration.toMinutes() % 60;
                long seconds = duration.getSeconds() % 60;
                ((Label) this.Listlabelchrono.get(this.i)).setText(String.format("%02d jours %02d heures %02d minutes %02d secondes", days, hours, minutes, seconds));
            }


            ((AnchorPane) this.Listpaneevent.get(this.i)).setVisible(true);

        }

        this.imageeventspanefx.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                String imageUrl = imageeventspanefx.getImage().getUrl();
                String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                int id = es.findbyImage(imageUrl);
                ListeventsController.holdIDs.value = Integer.toString(id);
                ListeventsController.this.es.readById(id);


                try {System.out.print(id);
                    loadFXML("/eventdetails.fxml");
                } catch (IOException var7) {
                    var7.printStackTrace();
                }

            }
        });
        this.imageeventspanefx1.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                String imageUrl = imageeventspanefx1.getImage().getUrl();
                String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                int id = es.findbyImage(imageUrl);
                ListeventsController.holdIDs.value = Integer.toString(id);
                ListeventsController.this.es.readById(id);;
                try {
                    System.out.print(id);
                    loadFXML("/eventdetails.fxml");
                } catch (IOException var7) {
                    var7.printStackTrace();
                }

            }
        });
        this.imageeventspanefx11.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                String imageUrl = imageeventspanefx11.getImage().getUrl();
                String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                int id = es.findbyImage(imageUrl);
                ListeventsController.holdIDs.value = Integer.toString(id);
                ListeventsController.this.es.readById(id);
                try {
                    System.out.print(id);
                    loadFXML("/eventdetails.fxml");
                } catch (IOException var7) {
                    var7.printStackTrace();
                }

            }
        });




    }




    @FXML
    private void viewmore(ActionEvent event) {
        int Nombre = 0;

        try {
            Nombre = this.es.numberevent();
        } catch (SQLException var6) {
            var6.getMessage();
        }

        this.CurrentEvent += 3;
        int diff = Nombre - this.CurrentEvent;
        if (diff == 2) {
            --this.CurrentEvent;
        } else if (diff == 1) {
            this.CurrentEvent -= 2;
        } else if (diff <= 0) {
            this.CurrentEvent = 0;
        }

        try {
            this.getUserData(this.CurrentEvent);
        } catch (SQLException var5) {
            var5.printStackTrace();
        }
    }
    private void loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void ini(event ev, ImageView qrImageView) throws SQLException {
        String myWeb = "Nom de l'event= " + ev.getTitre() + " , = " + ev.getDescription();
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

        // Set the QR code image to the corresponding ImageView
        qrImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
    }



}