package controllers;

import entities.event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
    private AnchorPane liste_events;
    public ArrayList<ImageView> ListImagese;
    public ArrayList<Label> Listlabeltitleevent;
    public ArrayList<Label> Listlabeldateevent;
    public ArrayList<Label> Listlabelheurevent;
    private eventService eventService;

    private ObservableList<event> eventData;
    int i = 0;
    int CurrentEvent = 0;
    private ObservableList<event> data;
    eventService es = new eventService();
    public ArrayList<AnchorPane> Listpaneevent;
    Connection con = null;
    public  static Holder<String> holdIDs = new Holder();


    public ListeventsController() {
        this.ListImagese = new ArrayList();
        this.Listpaneevent = new ArrayList();
        this.Listlabeltitleevent = new ArrayList();
        this.Listlabeldateevent= new ArrayList();
        this.Listlabelheurevent= new ArrayList();
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



}