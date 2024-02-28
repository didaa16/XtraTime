package controllers;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import entities.Equipement;
import entities.Reservation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import services.ServiceEquipement;
import services.ServiceReservation;
import javafx.collections.FXCollections;

import javax.swing.*;

public class listReservation {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Reservation, String> clientpseudoReservation;

    @FXML
    private TableColumn<Reservation, String> dateReservation;

    @FXML
    private TableColumn<Reservation, String> dureeReservation;

    @FXML
    private TableColumn<Reservation, String> equReservation;

    @FXML
    private TableColumn<Reservation, Integer> idReservation;

    @FXML
    private TableColumn<Reservation, Integer> prixReservation;
    @FXML
    private TableView<Reservation> tableReservation;

    @FXML
    private TableColumn<Reservation, Integer> terrainIdReservation;
    @FXML
    private TextField idSelected;

    int index = -1;
    private ServiceReservation serviceReservation;
    private static int id;
    public static void setIdRes(int idRes){id = idRes ;}
    @FXML
    void initialize() {
        serviceReservation = new ServiceReservation();
        List<Reservation> listeR;
        try {
            listeR = serviceReservation.afficher();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        idReservation.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateReservation.setCellValueFactory(new PropertyValueFactory<>("date"));
        dureeReservation.setCellValueFactory(new PropertyValueFactory<>("duree"));
        prixReservation.setCellValueFactory(new PropertyValueFactory<>("prix"));
        terrainIdReservation.setCellValueFactory(new PropertyValueFactory<>("terrainId"));
        clientpseudoReservation.setCellValueFactory(new PropertyValueFactory<>("clientPseudo"));
        equReservation.setCellValueFactory(new PropertyValueFactory<>("equipements"));
        ObservableList<Reservation> observableList = FXCollections.observableArrayList(listeR);
        tableReservation.setItems(observableList);

    }
    private void refreshTableView() {
        ObservableList<Reservation> liste = null;
        try {
            liste = FXCollections.observableList(serviceReservation.afficher());
            tableReservation.setItems(liste);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void suppReservationOnClick(ActionEvent actionEvent) {
        if (index != -1) {
            try {
                idSelected.setText(String.valueOf(id));
                serviceReservation.supprimer(id);
                JOptionPane.showMessageDialog(null,"Equipement Supprimé avec succès !");
                refreshTableView(); // Rafraîchir la TableView après la suppression
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la suppression de l'équipement : " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null,"Veuillez sélectionner un équipement à supprimer !");
        }
    }
    @FXML
    void getSelected(MouseEvent event) {
        index = tableReservation.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        Reservation reservationSelectionnee = tableReservation.getItems().get(index);
        idSelected.setText(String.valueOf(reservationSelectionnee.getId()));
        setIdRes(reservationSelectionnee.getId());
    }
}
