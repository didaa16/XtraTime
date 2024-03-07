package controllers.utilisateur;

import entities.utilisateur.Utilisateur;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.utilisateur.ServiceUtilisateurs;
import utils.Encryptor;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class dashboard {


@FXML
private AnchorPane anchorMain;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;

    //BORDER TOP
        @FXML
        private Button Menu;
        @FXML
        private Button MenuClose;
        @FXML
        private Button logo;
        @FXML
        private MenuItem deconnecterButton;


    //BORDER LEFT
        @FXML
        private Button utilisateursButton;
        @FXML
        private AnchorPane slider;


    //BORDER CENTER

    @FXML
    private AnchorPane statsAnchor;

        //ANCHOR PANE USERS
        @FXML
        private AnchorPane utilisateursAnchorPane;

            //HBOX (RECHERCHE, AJOUTER ADMIN, MODIFIER, SUPPRIMER)
            @FXML
            private Button modifierButton, supprimerButton, rechercheButton, ajouterButton;
            @FXML
            private TextField rechercheDashboard;

            //HBOX BELOW THE TABLE VIEW (CONTAINS RADIO BOXES)
            @FXML
            private ToggleGroup roles;
            @FXML
            private RadioButton admins, clients, locateurs, livreurs;

            //TABLE VIEW
            @FXML
            private TableView<Utilisateur> TableView;
            @FXML
            private TableColumn<Utilisateur, String> pseudoC;
            @FXML
            private TableColumn<Utilisateur, Integer> cinC;
            @FXML
            private TableColumn<Utilisateur, Integer> nomC;
            @FXML
            private TableColumn<Utilisateur, String> prenomC;
            @FXML
            private TableColumn<Utilisateur, Integer> ageC;
            @FXML
            private TableColumn<Utilisateur, Integer> numTelC;
            @FXML
            private TableColumn<Utilisateur, String> emailC;

            //GRIDPANE
            @FXML
            private TextField pseudoDashboard, cinDashboard, nomDashboard, prenomDashboard, ageDashboard, numtelDashboard, emailDashboard;
            @FXML
            private ToggleGroup role;
            @FXML
            private RadioButton roleClientDashboard, roleLocateurDashboard;
            @FXML
            private Label pseudoDashboardError, cinDashboardError, nomDashboardError, prenomDashboardError, ageDashboardError, numtelDashboardError, emailDashboardError, roleDashboardError;


        //ANCHOR PANE ADD ADMIN, PROFILE
        @FXML
        private AnchorPane ajouterAnchorPane;
            @FXML
            private TextField pseudoAjout, cinAjout, nomAjouter, prenomAjout, ageAjout, numtelAjout, emailAjout, mdpAjout;
            @FXML
            private PasswordField confirmMdpAjout;
            @FXML
            private Label complexiteLabel1, pseudoError, cinError, nomError, prenomError, ageError, numtelError, emailError, mdpError, confirmMdpError, mdpLabel, confirmMdpLabel, roleAddError;
            @FXML
            private Button annulerButton, ajoutAdminButton;
            @FXML
            private RadioButton roleAdminSignup, roleLivreurSignup;
            @FXML
            private ToggleGroup role2;
            @FXML
            private ProgressBar complexiteBar1;


        //ANCHOR PANE MODIFIER MDP
        @FXML
        private AnchorPane anchorPaneModifierMdp;
            @FXML
            private PasswordField ancienText, nouveauMdp, confirmNouveauMdp;
            @FXML
            private Label ancienError, nouveauMdpError, confirmNouveauMdpError, complexiteLabel;
            @FXML
            private Button confirmerNouveauMdp;
            @FXML
            private ProgressBar complexiteBar;

    ServiceUtilisateurs serviceUtilisateurs;
    Encryptor encryptor = new Encryptor();
    private int index = -1;
    private static Utilisateur loggedInUser;
    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }
    private static List<Utilisateur>deletedUtilisateurs = new ArrayList<>();

    void updateData(){
        pseudoDashboard.setDisable(true);
        serviceUtilisateurs = new ServiceUtilisateurs();
        afficherUtilisateursParRole("Admin");
        admins.setOnAction(event -> afficherUtilisateursParRole("Admin"));
        clients.setOnAction(event -> afficherUtilisateursParRole("Client"));
        locateurs.setOnAction(event -> afficherUtilisateursParRole("Locateur"));
        livreurs.setOnAction(event -> afficherUtilisateursParRole("Livreur"));
    }
    @FXML
    void initialize() {
        logo.setOnAction(event -> {
            statsAnchor.setVisible(true);
            utilisateursAnchorPane.setVisible(false);
            ajouterAnchorPane.setVisible(false);
            anchorPaneModifierMdp.setVisible(false);
        });
        deconnecterButton.setOnAction(event -> {
            try {
                loggedInUser = null;
                Parent root = FXMLLoader.load(getClass().getResource("/FxmlUtilisateur/login.fxml"));
                TableView.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        MenuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });
        updateData();
        modifierButton.setDisable(true);
        supprimerButton.setDisable(true);
        getComplexite();
        getComplexite1();
        updateChart();
        TableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Utilisateur> change) -> {
            if (change.getList().size() > 0 && change.getList().get(0) != null && change.getList().get(0).equals(KeyCode.CONTROL)) {
                recupererPseudos();
            }
        });
    }
    @FXML
    private void recupererPseudos() {
        List<Utilisateur> utilisateursSelectionnes = TableView.getSelectionModel().getSelectedItems();
        deletedUtilisateurs.clear();
        deletedUtilisateurs.addAll(utilisateursSelectionnes);
    }
    private void afficherUtilisateursParRole(String role) {
        ObservableList<Utilisateur> listeUtilisateurs = FXCollections.observableList(serviceUtilisateurs.afficherParRole(role));
        pseudoC.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
        cinC.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomC.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomC.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageC.setCellValueFactory(new PropertyValueFactory<>("age"));
        numTelC.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        emailC.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableView.setItems(listeUtilisateurs);
    }
    @FXML
    void getSelected(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            index = TableView.getSelectionModel().getSelectedIndex();
            if (index <= -1){
                return;
            }
            Utilisateur utilisateurSelectionne = TableView.getItems().get(index);
            pseudoDashboard.setText(utilisateurSelectionne.getPseudo());
            cinDashboard.setText(String.valueOf(utilisateurSelectionne.getCin()));
            nomDashboard.setText(utilisateurSelectionne.getNom());
            prenomDashboard.setText(utilisateurSelectionne.getPrenom());
            ageDashboard.setText(String.valueOf(utilisateurSelectionne.getAge()));
            numtelDashboard.setText(String.valueOf(utilisateurSelectionne.getNumtel()));
            emailDashboard.setText(utilisateurSelectionne.getEmail());
            switch (utilisateurSelectionne.getRole()){
                case "Admin", "Livreur":
                    roleClientDashboard.setSelected(false);
                    roleLocateurDashboard.setSelected(false);
                    roleClientDashboard.setDisable(true);
                    roleLocateurDashboard.setDisable(true);
                    break;
                case "Client":
                    roleClientDashboard.setDisable(false);
                    roleLocateurDashboard.setDisable(false);
                    roleClientDashboard.setSelected(true);
                    cardClient.setLoggedInUser(utilisateurSelectionne);
                    // Open the new scene if the role is "Client"
                    openCardClientScene();
                    break;
                case "Locateur":
                    roleClientDashboard.setDisable(false);
                    roleLocateurDashboard.setDisable(false);
                    roleLocateurDashboard.setSelected(true);
                    break;
                default:
                    break;
            }
            modifierButton.setDisable(false);
            supprimerButton.setDisable(false);
        }
    }

    private void openCardClientScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlUtilisateur/cardClient.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getErrors(){
        pseudoDashboardError.setText("");
        cinDashboardError.setText("");
        nomDashboardError.setText("");
        prenomDashboardError.setText("");
        ageDashboardError.setText("");
        numtelDashboardError.setText("");
        emailDashboardError.setText("");
        if(pseudoDashboard.getText().isBlank()){
            pseudoDashboardError.setTextFill(Color.RED);
            pseudoDashboardError.setText("Le Pseudo est invalide");
            return true;
        }
        if(cinDashboard.getText().isBlank() || !cinDashboard.getText().matches("\\d{1,9}")){
            cinDashboardError.setTextFill(Color.RED);
            cinDashboardError.setText("Le CIN est invalide");
            return true;
        }
        if(nomDashboard.getText().isBlank() || !nomDashboard.getText().matches("[a-zA-Z ]+")){
            nomDashboardError.setTextFill(Color.RED);
            nomDashboardError.setText("Le nom est invalide");
            return true;
        }

        if(prenomDashboard.getText().isBlank() || !prenomDashboard.getText().matches("[a-zA-Z ]+")){
            prenomDashboardError.setTextFill(Color.RED);
            prenomDashboardError.setText("Le prénom est invalide");
            return true;
        }
        if(ageDashboard.getText().isBlank() || !ageDashboard.getText().matches("\\d+") || Integer.parseInt(ageDashboard.getText()) < 18){
            ageDashboardError.setTextFill(Color.RED);
            ageDashboardError.setText("L'âge doit être un nombre valide et être supérieur à 18");
            return true;
        }
        if(numtelDashboard.getText().isBlank() || !numtelDashboard.getText().matches("\\d{1,12}")){
            numtelDashboardError.setTextFill(Color.RED);
            numtelDashboardError.setText("Le numéro de téléphone est invalide");
            return true;
        }
        if(emailDashboard.getText().isBlank() || !emailDashboard.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            emailDashboardError.setTextFill(Color.RED);
            emailDashboardError.setText("L'email est invalide");
            return true;
        }
        return false;
    }
    @FXML
    public void ModifierButtonButtonOnClick(ActionEvent event){
        if (!getErrors()) {
            try {
                Utilisateur newUser1 = serviceUtilisateurs.afficherParPseudo(pseudoDashboard.getText());
                Utilisateur newUser = new Utilisateur(newUser1.getPseudo(), Integer.parseInt(cinDashboard.getText()), nomDashboard.getText(),
                        prenomDashboard.getText(), Integer.parseInt(ageDashboard.getText()), Integer.parseInt(numtelDashboard.getText()),
                        emailDashboard.getText(), newUser1.getMdp(), (roleClientDashboard.isSelected() ? "Client" : (roleLocateurDashboard.isSelected() ? "Locateur" : newUser1.getRole())));
                serviceUtilisateurs.modifier(newUser);
                updateChart();
                JOptionPane.showMessageDialog(null,"Modification effectuée! ");
                updateData();
                switch (newUser.getRole()){
                    case "Admin":
                        admins.setSelected(true);
                        break;
                    case "Client":
                        clients.setSelected(true);
                        break;
                    case "Locateur":
                        locateurs.setSelected(true);
                        break;
                    case "Livreur":
                        livreurs.setSelected(true);
                        break;
                    default:
                        break;
                }
                afficherUtilisateursParRole(newUser.getRole());
                TableView.refresh();
                System.out.println("Utilisateur modifié avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
            }

        }
    }
    @FXML
    public void SupprimerButtonButtonOnClick(ActionEvent event){
        try {
            List<Utilisateur> utilisateursSelectionnes = TableView.getSelectionModel().getSelectedItems();
            if (utilisateursSelectionnes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Aucun utilisateur sélectionné !");
                return;
            }
            String role = "";
            for (Utilisateur utilisateur : utilisateursSelectionnes) {
                if (utilisateur.equals(loggedInUser)) {
                    JOptionPane.showMessageDialog(null, "Il s'agit de votre compte ! Vous ne pouvez pas le supprimer !");
                    return;
                }
            }
            int choix = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer ces utilisateurs ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
            if (choix == JOptionPane.YES_OPTION) {
                for (Utilisateur utilisateur : utilisateursSelectionnes) {
                    role = utilisateur.getRole();
                    serviceUtilisateurs.supprimer(utilisateur);
                }
                updateChart();
                JOptionPane.showMessageDialog(null,"Utilisateurs supprimés avec succès !");
                updateData();
                TableView.refresh();
                switch (role){
                    case "Admin":
                        admins.setSelected(true);
                        break;
                    case "Client":
                        clients.setSelected(true);
                        break;
                    case "Locateur":
                        locateurs.setSelected(true);
                        break;
                    case "Livreur":
                        livreurs.setSelected(true);
                        break;
                    default:
                        break;
                }
                afficherUtilisateursParRole(role);
                System.out.println("Utilisateurs supprimés avec succès !");
            } else {
                System.out.println("Suppression annulée par l'utilisateur.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des utilisateurs : " + e.getMessage());
        }
    }
    @FXML
    public void rechercheButtonOnClick(ActionEvent event){
        String data = rechercheDashboard.getText().toString();
        admins.setSelected(false);
        clients.setSelected(false);
        locateurs.setSelected(false);
        livreurs.setSelected(false);
        ObservableList<Utilisateur> listeUtilisateurs = FXCollections.observableList(serviceUtilisateurs.recherche(data));
        pseudoC.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
        cinC.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomC.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomC.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageC.setCellValueFactory(new PropertyValueFactory<>("age"));
        numTelC.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        emailC.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableView.setItems(listeUtilisateurs);
        modifierButton.setDisable(true);
        supprimerButton.setDisable(true);
    }

    @FXML
    private void utilisateursButtonOnClick(ActionEvent event){
        utilisateursAnchorPane.setVisible(true);
        ajouterAnchorPane.setVisible(false);
        anchorPaneModifierMdp.setVisible(false);
        statsAnchor.setVisible(false);
    }
    @FXML
    private void ajouterButtonOnClick(ActionEvent event){
        utilisateursAnchorPane.setVisible(false);
        ajouterAnchorPane.setVisible(true);
        statsAnchor.setVisible(false);
    }
    public boolean getErrors1(){
        pseudoError.setText("");
        cinError.setText("");
        nomError.setText("");
        prenomError.setText("");
        ageError.setText("");
        numtelError.setText("");
        emailError.setText("");
        mdpError.setText("");
        confirmMdpError.setText("");

        if(pseudoAjout.getText().isBlank()){
            pseudoError.setTextFill(Color.RED);
            pseudoError.setText("Le Pseudo est invalide");
            return true;
        }
        if(cinAjout.getText().isBlank() || !cinAjout.getText().matches("\\d{1,9}")){
            cinError.setTextFill(Color.RED);
            cinError.setText("Le CIN est invalide");
            return true;
        }
        if(nomAjouter.getText().isBlank() || !nomAjouter.getText().matches("[a-zA-Z ]+")){
            nomError.setTextFill(Color.RED);
            nomError.setText("Le nom est invalide");
            return true;
        }
        if(prenomAjout.getText().isBlank() || !prenomAjout.getText().matches("[a-zA-Z ]+")){
            prenomError.setTextFill(Color.RED);
            prenomError.setText("Le prénom est invalide");
            return true;
        }
        if(ageAjout.getText().isBlank() || !ageAjout.getText().matches("\\d+") || Integer.parseInt(ageAjout.getText()) < 18){
            ageError.setTextFill(Color.RED);
            ageError.setText("L'âge doit être un nombre valide et être supérieur à 18");
            return true;
        }
        if(numtelAjout.getText().isBlank() || !numtelAjout.getText().matches("\\d{1,12}")){
            numtelError.setTextFill(Color.RED);
            numtelError.setText("Le numéro de téléphone est invalide");
            return true;
        }
        if(emailAjout.getText().isBlank() || !emailAjout.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            emailError.setTextFill(Color.RED);
            emailError.setText("L'email est invalide");
            return true;
        }
        if(mdpAjout.getText().isBlank()|| complexiteLabel1.getText().equals("Faible") || complexiteLabel1.getText().equals("Très Faible")){
            mdpError.setTextFill(Color.RED);
            mdpError.setText("Le mot de passe est invalide");
            return true;
        }
        if(confirmMdpAjout.getText().isBlank()){
            confirmMdpError.setTextFill(Color.RED);
            confirmMdpError.setText("La confirmation du mot de passe est invalide");
            return true;
        }
        if(!Objects.equals(confirmMdpAjout.getText(), mdpAjout.getText())){
            confirmMdpError.setTextFill(Color.RED);
            confirmMdpError.setText("Le mot de passe doit etre le meme");
            return true;
        }
        if(!roleAdminSignup.isSelected() && !roleLivreurSignup.isSelected()){
            roleAddError.setTextFill(Color.RED);
            roleAddError.setText("Le role est obligatoire");
            return true;
        }
        try {
            if (serviceUtilisateurs.pseudoExiste(pseudoAjout.getText())){
                pseudoError.setTextFill(Color.RED);
                pseudoError.setText("Ce pseudo est déjà utilisé, veuillez en choisir un autre");
                return true;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    @FXML
    private void ajoutAdminButtonOnClick(ActionEvent event) throws NoSuchAlgorithmException{
        if (!getErrors1()) {
            Utilisateur newUser = new Utilisateur(pseudoAjout.getText(), Integer.parseInt(cinAjout.getText()), nomAjouter.getText(),
                    prenomAjout.getText(), Integer.parseInt(ageAjout.getText()), Integer.parseInt(numtelAjout.getText()), emailAjout.getText(),
                    encryptor.encryptString(mdpAjout.getText()), (roleAdminSignup.isSelected() ? "Admin" : "Livreur"));
            try {
                serviceUtilisateurs.ajouter(newUser);
                updateChart();
                System.out.println("Utilisateur ajouté avec succès !");
                JOptionPane.showMessageDialog(null,"Utilisateur ajouté avec succès !");
                updateData();
                TableView.refresh();
                afficherUtilisateursParRole(newUser.getRole());
                switch (newUser.getRole()){
                    case "Admin":
                        admins.setSelected(true);
                        break;
                    case "Client":
                        clients.setSelected(true);
                        break;
                    case "Locateur":
                        locateurs.setSelected(true);
                        break;
                    case "Livreur":
                        livreurs.setSelected(true);
                        break;
                    default:
                        break;
                }
                ajouterAnchorPane.setVisible(false);
                utilisateursAnchorPane.setVisible(true);
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            }

        }
    }
    @FXML
    private void profileButtonOnClick(ActionEvent event){
        statsAnchor.setVisible(false);
        utilisateursAnchorPane.setVisible(false);
        anchorPaneModifierMdp.setVisible(false);
        ajouterAnchorPane.setVisible(true);
        pseudoAjout.setDisable(true);
        cinAjout.setDisable(true);
        nomAjouter.setDisable(true);
        prenomAjout.setDisable(true);
        ageAjout.setDisable(true);
        numtelAjout.setDisable(true);
        emailAjout.setDisable(true);
        mdpAjout.setDisable(true);
        confirmMdpAjout.setDisable(true);
        pseudoAjout.setText(loggedInUser.getPseudo());
        cinAjout.setText(String.valueOf(loggedInUser.getCin()));
        nomAjouter.setText(loggedInUser.getNom());
        prenomAjout.setText(loggedInUser.getPrenom());
        ageAjout.setText(String.valueOf(loggedInUser.getAge()));
        numtelAjout.setText(String.valueOf(loggedInUser.getNumtel()));
        emailAjout.setText(loggedInUser.getEmail());
        mdpLabel.setVisible(false);
        confirmMdpLabel.setVisible(false);
        mdpAjout.setVisible(false);
        confirmMdpAjout.setVisible(false);
        ajoutAdminButton.setVisible(false);
        annulerButton.setVisible(false);
        roleLivreurSignup.setVisible(false);
        roleAdminSignup.setVisible(false);
    }
    @FXML
    private void deconnecterButtonOnClick(ActionEvent event){
        loggedInUser = null;
        serviceUtilisateurs.changeScreen(event, "/FxmlUtilisateur/login.fxml", "LOGIN");
    }
    @FXML
    private void modifierMdpOnClick(ActionEvent event){
        statsAnchor.setVisible(false);
        ajouterAnchorPane.setVisible(false);
        utilisateursAnchorPane.setVisible(false);
        anchorPaneModifierMdp.setVisible(true);
    }
    private boolean getErrorsMdp() throws NoSuchAlgorithmException {
        String thisUserMdp = loggedInUser.getMdp();
        String mdpSaisi = encryptor.encryptString(ancienText.getText());
        if(!Objects.equals(thisUserMdp, mdpSaisi)){
            ancienError.setTextFill(Color.RED);
            ancienError.setText("Ancien Mot de passe invalide");
            return true;
        }
        if(nouveauMdp.getText().isBlank()|| complexiteLabel.getText().equals("Faible") || complexiteLabel.getText().equals("Très Faible")){
            nouveauMdpError.setTextFill(Color.RED);
            nouveauMdpError.setText("Le mot de passe est invalide");
            return true;
        }
        if(confirmNouveauMdp.getText().isBlank()){
            confirmNouveauMdpError.setTextFill(Color.RED);
            confirmNouveauMdpError.setText("La confirmation du mot de passe est invalide");
            return true;
        }
        if(!Objects.equals(confirmNouveauMdp.getText(), nouveauMdp.getText())){
            confirmNouveauMdpError.setTextFill(Color.RED);
            confirmNouveauMdpError.setText("Le mot de passe doit etre le meme");
            return true;
        }
        return false;
    }
    @FXML
    private void confirmerNouveauMdpOnClick(ActionEvent event) throws NoSuchAlgorithmException {
        if (!getErrorsMdp()){
            try {
                serviceUtilisateurs.modifierMdp(loggedInUser, encryptor.encryptString(nouveauMdp.getText()));
                JOptionPane.showMessageDialog(null,"Mot de Passe modifié avec succès !");
                anchorPaneModifierMdp.setVisible(false);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @FXML
    private void getComplexite() {
        nouveauMdp.textProperty().addListener((observable, oldValue, newValue) -> {
            complexiteBar.setVisible(true);
            complexiteLabel.setText("");
            complexiteBar.setProgress(0);
            complexiteBar.setBlendMode(null);
            boolean hasSpecialChars = newValue.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
            boolean hasDigits = newValue.matches(".*\\d.*");
            boolean hasLowercase = newValue.matches(".*[a-z].*");
            boolean hasUppercase = newValue.matches(".*[A-Z].*");
            int complexityScore = 0;
            if (hasSpecialChars) complexityScore += 2;
            if (hasDigits) complexityScore += 2;
            if (hasLowercase) complexityScore += 2;
            if (hasUppercase) complexityScore += 2;
            if (newValue.length() < 8) {
                complexiteLabel.setText("Faible");
                complexiteBar.setProgress(0.0);
                complexiteBar.setBlendMode(BlendMode.RED);
            } else {
                if (complexityScore >= 8) {
                    complexiteLabel.setText("Très Fort");
                    complexiteBar.setProgress(1.0);
                    complexiteBar.setBlendMode(BlendMode.COLOR_DODGE);
                } else if (complexityScore >= 6) {
                    complexiteLabel.setText("Fort");
                    complexiteBar.setProgress(0.75);
                    complexiteBar.setBlendMode(BlendMode.COLOR_DODGE);
                } else if (complexityScore >= 4) {
                    complexiteLabel.setText("Moyenne");
                    complexiteBar.setProgress(0.5);
                    complexiteBar.setBlendMode(BlendMode.HARD_LIGHT);
                } else if (complexityScore >= 2) {
                    complexiteLabel.setText("Faible");
                    complexiteBar.setProgress(0.25);
                    complexiteBar.setBlendMode(BlendMode.RED);
                } else {
                    complexiteLabel.setText("Très Faible");
                    complexiteBar.setProgress(0.0);
                    complexiteBar.setBlendMode(BlendMode.RED);
                }
            }
        });
    }
    @FXML
    private void getComplexite1() {
        mdpAjout.textProperty().addListener((observable, oldValue, newValue) -> {
            complexiteBar1.setVisible(true);

            // Reset complexity label and bar color
            complexiteLabel1.setText("");
            complexiteBar1.setProgress(0);
            complexiteBar1.setBlendMode(null);

            // Check if the password contains special characters
            boolean hasSpecialChars = newValue.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
            // Check if the password contains digits
            boolean hasDigits = newValue.matches(".*\\d.*");
            // Check if the password contains lowercase letters
            boolean hasLowercase = newValue.matches(".*[a-z].*");
            // Check if the password contains uppercase letters
            boolean hasUppercase = newValue.matches(".*[A-Z].*");

            // Calculate complexity score
            int complexityScore = 0;
            if (hasSpecialChars) complexityScore += 2;
            if (hasDigits) complexityScore += 2;
            if (hasLowercase) complexityScore += 2;
            if (hasUppercase) complexityScore += 2;

            // Check if the password length is at least 8 characters
            if (newValue.length() < 8) {
                complexiteLabel1.setText("Faible");
                complexiteBar1.setProgress(0.0);
                complexiteBar1.setBlendMode(BlendMode.RED);
            } else {
                // Set complexity level and progress bar based on score
                if (complexityScore >= 8) {
                    complexiteLabel1.setText("Très Fort");
                    complexiteBar1.setProgress(1.0);
                    complexiteBar1.setBlendMode(BlendMode.COLOR_DODGE);
                } else if (complexityScore >= 6) {
                    complexiteLabel1.setText("Fort");
                    complexiteBar1.setProgress(0.75);
                    complexiteBar1.setBlendMode(BlendMode.COLOR_DODGE);
                } else if (complexityScore >= 4) {
                    complexiteLabel1.setText("Moyenne");
                    complexiteBar1.setProgress(0.5);
                    complexiteBar1.setBlendMode(BlendMode.HARD_LIGHT);
                } else if (complexityScore >= 2) {
                    complexiteLabel1.setText("Faible");
                    complexiteBar1.setProgress(0.25);
                    complexiteBar1.setBlendMode(BlendMode.RED);
                } else {
                    complexiteLabel1.setText("Très Faible");
                    complexiteBar1.setProgress(0.0);
                    complexiteBar1.setBlendMode(BlendMode.RED);
                }
            }
        });
    }
    private void pieChart (){
        List<Utilisateur> user = serviceUtilisateurs.afficher();
        List<Utilisateur> admins = serviceUtilisateurs.afficherParRole("Admin");
        List<Utilisateur> clients = serviceUtilisateurs.afficherParRole("Client");
        List<Utilisateur> locateurs = serviceUtilisateurs.afficherParRole("Locateur");
        List<Utilisateur> livreurs = serviceUtilisateurs.afficherParRole("Livreur");
        float admin = (float) admins.size() /user.size();
        float client = (float) clients.size()/user.size();
        float locateur = (float) locateurs.size()/user.size();
        float livreur = (float) livreurs.size()/user.size();
        float adminP = admin*100;
        float clientP = client*100;
        float locateurP = locateur*100;
        float livreurP = livreur*100;

        ObservableList<PieChart.Data> pie = FXCollections.observableArrayList(
                new PieChart.Data("ADMINS : " + String.valueOf(adminP) + " %", admin),
                new PieChart.Data("CLIENTS : " + String.valueOf(clientP) + " %", client),
                new PieChart.Data("LOCATEURS : " + String.valueOf(locateurP) + " %", locateur),
                new PieChart.Data("LIVREURS : " + String.valueOf(livreurP) + " %", livreur)
        );
        PieChart pieChart = new PieChart(pie);
        pieChart.setTitle("ROLES");
        pieChart.setClockwise(true);
        pieChart.setAnimated(true);
        pieChart.setLabelLineLength(50);
        pieChart.setVisible(true);
        pieChart.setStartAngle(180);
        statsAnchor.getChildren().add(pieChart);
    }
    private void updateChart(){
        statsAnchor.getChildren().clear();
        pieChart();
    }
    @FXML
    void recherche(){
        String keyword = rechercheDashboard.getText().toLowerCase();
        ObservableList<Utilisateur> filteredList = FXCollections.observableArrayList();
        if (keyword.isEmpty()) {
            TableView.setItems(FXCollections.observableList(serviceUtilisateurs.afficher()));
            return;
        }
        for (Utilisateur utilisateur : TableView.getItems()) {
            String cin = String.valueOf(utilisateur.getCin());
            String age = String.valueOf(utilisateur.getAge());
            String numTel = String.valueOf(utilisateur.getNumtel());
            if (    utilisateur.getPseudo().toLowerCase().contains(keyword) ||
                    cin.toLowerCase().contains(keyword) ||
                    utilisateur.getNom().toLowerCase().contains(keyword) ||
                    utilisateur.getPrenom().toLowerCase().contains(keyword)||
                    age.toLowerCase().contains(keyword) ||
                    utilisateur.getEmail().toLowerCase().contains(keyword) ||
                    numTel.toLowerCase().contains(keyword)
            ){
                filteredList.add(utilisateur);
            }
        }
        TableView.setItems(filteredList);
    }

    @FXML
    private void eventsButtonOnClick(ActionEvent event) throws IOException {
        initialize();
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource("/fxmlevent/eventback.fxml"));
        anchorMain.getChildren().add(root);
    }
    @FXML
    private void sponsosButtonOnClick(ActionEvent event) throws IOException {
        initialize();
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource("/fxmlevent/sponsoback.fxml"));
        anchorMain.getChildren().add(root);
    }
    @FXML
    private void abonnementsButtonOnClick(ActionEvent event) throws IOException {
        initialize();
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource("/FXMLAbonnement/AfficherA.fxml"));
        anchorMain.getChildren().add(root);
    }
    @FXML
    private void packsButtonOnClick(ActionEvent event) throws IOException {
        initialize();
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource("/FXMLAbonnement/Ajout.fxml"));
        anchorMain.getChildren().add(root);
    }
    @FXML
    private void produitsButtonOnClick(ActionEvent event) throws IOException {
        initialize();
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/AjouterProduit.fxml"));
        anchorMain.getChildren().add(root);
    }
    @FXML
    private void commandesButtonOnClick(ActionEvent event) throws IOException {
        initialize();
        anchorMain.getChildren().clear();
        Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/GestionCommande.fxml"));
        anchorMain.getChildren().add(root);
    }




}
