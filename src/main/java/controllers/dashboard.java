package controllers;
import entities.Utilisateur;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceUtilisateurs;

import java.net.URL;
import java.util.ResourceBundle;

public class dashboard implements Initializable {
    ServiceUtilisateurs serviceUtilisateurs;
    ObservableList<Utilisateur> listAdmins;
    ObservableList<Utilisateur> listClients;
    ObservableList<Utilisateur> listeLocateurs;
    ObservableList<Utilisateur> listeLivreurs;
    @FXML
    private TableColumn<Utilisateur, Integer> adminsAge;

    @FXML
    private TableColumn<Utilisateur, Integer> adminsCin;

    @FXML
    private TableColumn<Utilisateur, String> adminsEmail;

    @FXML
    private TableColumn<Utilisateur, String> adminsNom;

    @FXML
    private TableColumn<Utilisateur, Integer> adminsNumTel;

    @FXML
    private TableColumn<Utilisateur, String> adminsPrenom;

    @FXML
    private TableColumn<Utilisateur, String> adminsPseudo;

    @FXML
    private TableView<Utilisateur> adminsTV;

    @FXML
    private TableColumn<Utilisateur, Integer> clientsAge;

    @FXML
    private TableColumn<Utilisateur, Integer> clientsCin;

    @FXML
    private TableColumn<Utilisateur, String> clientsEmail;

    @FXML
    private TableColumn<Utilisateur, String> clientsNom;

    @FXML
    private TableColumn<Utilisateur, Integer> clientsNumTel;

    @FXML
    private TableColumn<Utilisateur, String> clientsPrenom;

    @FXML
    private TableColumn<Utilisateur, String> clientsPseudo;

    @FXML
    private TableView<Utilisateur> clientsTV;

    @FXML
    private TableColumn<Utilisateur, Integer> livreursAge;

    @FXML
    private TableColumn<Utilisateur, Integer> livreursCin;

    @FXML
    private TableColumn<Utilisateur, String> livreursEmail;

    @FXML
    private TableColumn<Utilisateur, String> livreursNom;

    @FXML
    private TableColumn<Utilisateur, Integer> livreursNumTel;

    @FXML
    private TableColumn<Utilisateur, String> livreursPrenom;

    @FXML
    private TableColumn<Utilisateur, String> livreursPseudo;

    @FXML
    private TableView<Utilisateur> livreursTV;

    @FXML
    private TableColumn<Utilisateur, Integer> locateursAge;

    @FXML
    private TableColumn<Utilisateur, Integer> locateursCIn;

    @FXML
    private TableColumn<Utilisateur, String> locateursEmail;

    @FXML
    private TableColumn<Utilisateur, String> locateursNom;

    @FXML
    private TableColumn<Utilisateur, Integer> locateursNumTel;

    @FXML
    private TableColumn<Utilisateur, String> locateursPrenom;

    @FXML
    private TableColumn<Utilisateur, String> locateursPseudo;

    @FXML
    private TableView<Utilisateur> locateursTV;

    private void getTable(String Role, ObservableList<Utilisateur> l, TableColumn pseudo, TableColumn cin, TableColumn nom,TableColumn prenom, TableColumn age,TableColumn numTel, TableColumn email, TableView TV){
        l = serviceUtilisateurs.afficherParRole(Role);
        pseudo.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
        cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        numTel.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        TV.setItems(l);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serviceUtilisateurs = new ServiceUtilisateurs();
        getTable("Admin",listAdmins, adminsPseudo, adminsCin, adminsNom, adminsPrenom, adminsAge, adminsNumTel, adminsEmail, adminsTV);
        getTable("Client",listClients, clientsPseudo, clientsCin, clientsNom, clientsPseudo, clientsAge, clientsNumTel, clientsEmail, clientsTV);
        getTable("Locateur",listeLocateurs, locateursPseudo, locateursCIn, locateursNom, locateursPrenom, locateursAge, locateursNumTel, locateursEmail, locateursTV);
        getTable("Livreur",listeLivreurs, livreursPseudo, livreursCin, livreursNom, livreursPrenom, livreursAge, livreursNumTel, livreursEmail, livreursTV);
    }
}
