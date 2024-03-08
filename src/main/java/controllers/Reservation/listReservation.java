package controllers.Reservation;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import entities.Equipement;
import entities.Reservation;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
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
    private static List<Reservation>deletedReservations = new ArrayList<>();
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
        tableReservation.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableReservation.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Reservation> change) -> {
            if (change.getList().size() > 0 && change.getList().get(0) != null && change.getList().get(0).equals(KeyCode.CONTROL)) {
                recupererIds();
            }
        });

    }
    @FXML
    private void recupererIds() {
        List<Reservation> reservationsSelectionnes = tableReservation.getSelectionModel().getSelectedItems();
        deletedReservations.clear();
        deletedReservations.addAll(reservationsSelectionnes);
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
        try {
            List<Reservation> reservationsSelectionnes = tableReservation.getSelectionModel().getSelectedItems();
            if (reservationsSelectionnes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Aucun utilisateur sélectionné !");
                return;
            }
            int choix = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer ces reservations ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
            if (choix == JOptionPane.YES_OPTION) {
                for (Reservation res : reservationsSelectionnes) {
                    serviceReservation.supprimer(res.getId());
                }
                JOptionPane.showMessageDialog(null, "Utilisateurs supprimés avec succès !");
                refreshTableView();
                System.out.println("Utilisateurs supprimés avec succès !");
            }
        }catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des utilisateurs : " + e.getMessage());
        }







        /*if (index != -1) {
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
        }*/
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
