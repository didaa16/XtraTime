package controllers;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import entities.event;
import services.eventService;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import utils.DataSource;
import javafx.scene.control.Label;



public class ListeventsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Button bouton;

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
    private AnchorPane liste_events;
    public ArrayList<ImageView> ListImagese;
    public ArrayList<Label> Listlabeltitleevent;
    private eventService eventService;

    private ObservableList<event> eventData;
    int i = 0;
    int CurrentEvent = 0;
    private ObservableList<event> data;
    eventService es = new eventService();
    public ArrayList<AnchorPane> Listpaneevent;
    Connection con = null;


    public ListeventsController() {
        this.ListImagese = new ArrayList();
        this.Listpaneevent = new ArrayList();
        this.Listlabeltitleevent = new ArrayList();
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
        this.ListImagese.add(this.imageeventspanefx);
        this.ListImagese.add(this.imageeventspanefx1);
        this.ListImagese.add(this.imageeventspanefx11);
        this.Listlabeltitleevent.add(this.subjecteventspanefx);
        this.Listlabeltitleevent.add(this.subjecteventspanefx1);
        this.Listlabeltitleevent.add(this.subjecteventspanefx11);
        int Nombre = this.es.numberevent();
        for (this.i = CurrentEvent; this.i < CurrentEvent + 3; ++this.i) {
            System.out.println(((event) this.data.get(this.i)).getImage());
            Image image = new Image(((event) this.data.get(this.i)).getImage());
            ((ImageView) this.ListImagese.get(this.i)).setImage(image);
            ((Label)this.Listlabeltitleevent.get(this.i)).setText(((event)this.data.get(this.i)).getTitre());


            ((AnchorPane) this.Listpaneevent.get(this.i)).setVisible(true);
        }

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
}
