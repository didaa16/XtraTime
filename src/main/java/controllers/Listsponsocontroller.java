package controllers;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import entities.event;
import entities.sponso;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import services.sponsoService;
import utils.DataSource;

public class Listsponsocontroller {

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
    public ArrayList<ImageView> ListImagese;
    public ArrayList<Label> Listlabeltitleevent;

    private services.sponsoService sponsoService;

    private ObservableList<sponso> sponsoData;
    int i = 0;
    int CurrentEvent = 0;
    private ObservableList<sponso> data;
    sponsoService es = new sponsoService();
    public ArrayList<AnchorPane> Listpaneevent;
    Connection con = null;
    public Listsponsocontroller() {
        this.ListImagese = new ArrayList();
        this.Listpaneevent = new ArrayList();
        this.Listlabeltitleevent = new ArrayList();

    }

    @FXML
    void initialize() {
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

        //nom
        this.Listlabeltitleevent.add(this.subjecteventspanefx);
        this.Listlabeltitleevent.add(this.subjecteventspanefx1);
        this.Listlabeltitleevent.add(this.subjecteventspanefx11);



        int Nombre = this.es.numbersponso();
        for (this.i = CurrentEvent; this.i < CurrentEvent + 3; ++this.i) {
            System.out.println(((sponso) this.data.get(this.i)).getImage());
            Image image = new Image(((sponso) this.data.get(this.i)).getImage());
            ((ImageView) this.ListImagese.get(this.i)).setImage(image);
            ((Label)this.Listlabeltitleevent.get(this.i)).setText(((sponso)this.data.get(this.i)).getNom());


            ((AnchorPane) this.Listpaneevent.get(this.i)).setVisible(true);
        }


    }

    @FXML
    private void viewmore(ActionEvent event) {
        int Nombre = 0;

        try {
            Nombre = this.es.numbersponso();
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
