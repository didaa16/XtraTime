package services.utilisateur;

import controllers.utilisateur.loginController;
import services.IService;
import utils.Encryptor;
import entities.utilisateur.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.MyDataBase;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtilisateurs implements IService<Utilisateur> {
    Encryptor encryptor = new Encryptor();

    byte[] encryptionKey = {65, 12, 12, 12, 12, 12, 12, 12, 12,
            12, 12, 12, 12, 12, 12, 12 };
    private Connection cnx;
    private Statement st;
    private PreparedStatement pst;

    public ServiceUtilisateurs() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Utilisateur utilisateur) throws SQLException {
        String req = "INSERT INTO `utilisateurs`(`pseudo`, `cin`, `nom`, `prenom`, `age`, `numTel`, `email`, `mdp`, `role`) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps= cnx.prepareStatement(req);
        ps.setString(1, utilisateur.getPseudo());
        ps.setInt(2, utilisateur.getCin());
        ps.setString(3, utilisateur.getNom());
        ps.setString(4, utilisateur.getPrenom());
        ps.setInt(5, utilisateur.getAge());
        ps.setInt(6, utilisateur.getNumtel());
        ps.setString(7, utilisateur.getEmail());
        ps.setString(8, utilisateur.getMdp());
        ps.setString(9, utilisateur.getRole());
        ps.executeUpdate();
        System.out.println("Personne Ajoutée");
    }

    @Override
    public void supprimer(Utilisateur utilisateur) throws SQLException {
        String req = "DELETE FROM `utilisateurs` WHERE pseudo=?";
        PreparedStatement ps= cnx.prepareStatement(req);
        ps.setString(1, utilisateur.getPseudo());
        ps.executeUpdate();
        System.out.println("Personne Supprimée");
    }

    @Override
    public void modifier(Utilisateur utilisateur) throws SQLException {
        String req="UPDATE `utilisateurs` SET `cin`=?,`nom`=?,`prenom`=?,`age`=?,`numTel`=?,`email`=?,`mdp`=?, `role`=?  WHERE pseudo=?";
        PreparedStatement ps= cnx.prepareStatement(req);
        ps.setInt(1, utilisateur.getCin());
        ps.setString(2, utilisateur.getNom());
        ps.setString(3, utilisateur.getPrenom());
        ps.setInt(4, utilisateur.getAge());
        ps.setInt(5, utilisateur.getNumtel());
        ps.setString(6, utilisateur.getEmail());
        ps.setString(7, utilisateur.getMdp());
        ps.setString(8, utilisateur.getRole());
        ps.setString(9, utilisateur.getPseudo());
        ps.executeUpdate();
        System.out.println("Personne modifie");
    }

    @Override
    public List<Utilisateur> afficher(){
        List<Utilisateur> utilisateurs= new ArrayList<>();
        String req="SELECT * FROM `utilisateurs`";
        try {
            Statement st  = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()){
                Utilisateur u = new Utilisateur();
                u.setPseudo(rs.getString("PSEUDO"));
                u.setCin(rs.getInt("CIN"));
                u.setNom(rs.getString("NOM"));
                u.setPrenom(rs.getString("PRENOM"));
                u.setAge(rs.getInt("AGE"));
                u.setNumtel(rs.getInt("NUMTEL"));
                u.setEmail(rs.getString("EMAIL"));
                u.setMdp(rs.getString("MDP"));
                u.setRole(rs.getString("ROLE"));
                utilisateurs.add(u);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return utilisateurs;
    }


    public Utilisateur afficherParPseudo(String pseudo) throws SQLException {
        Utilisateur u = new Utilisateur();
        String req = "SELECT * FROM `utilisateurs` WHERE pseudo=?";
        PreparedStatement ps= cnx.prepareStatement(req);
        ps.setString(1, pseudo);
        ResultSet rs = ps.executeQuery(); // Remove req from executeQuery
        while (rs.next()){
            u = new Utilisateur(rs.getString("PSEUDO"), rs.getInt("CIN"), rs.getString("NOM"), rs.getString("PRENOM"), rs.getInt("AGE"), rs.getInt("NUMTEL"), rs.getString("EMAIL"), rs.getString("MDP"), rs.getString("ROLE"));
        }
        return u;
    }



    public List<Utilisateur> afficherParRole(String role){
        List<Utilisateur> utilisateurs = new ArrayList<>() ; // Initialize the ObservableList
        String req = "SELECT * FROM `utilisateurs` WHERE `role`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Utilisateur u = new Utilisateur();
                u.setPseudo(rs.getString("PSEUDO"));
                u.setCin(rs.getInt("CIN"));
                u.setNom(rs.getString("NOM"));
                u.setPrenom(rs.getString("PRENOM"));
                u.setAge(rs.getInt("AGE"));
                u.setNumtel(rs.getInt("NUMTEL"));
                u.setEmail(rs.getString("EMAIL"));
                u.setMdp(rs.getString("MDP"));
                u.setRole(rs.getString("ROLE"));
                utilisateurs.add(u);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return utilisateurs;
    }


    public boolean utilisateurLoggedIn(String pseudo, String mdp) throws SQLException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String req = "SELECT mdp FROM `utilisateurs` WHERE pseudo=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, pseudo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String mdpCrypteDB = rs.getString("mdp");
            String mdpCrypteInput = encryptor.encryptString(mdp);
            return mdpCrypteInput.equals(mdpCrypteDB);
        } else {
            return false;
        }
    }


    public boolean pseudoExiste(String pseudo) throws SQLException {
        String req = "SELECT * FROM `utilisateurs` WHERE pseudo=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, pseudo);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public void changeScreen(ActionEvent event, String fxmlFile, String title){
        try {
            FXMLLoader loader = new FXMLLoader(loginController.class.getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<Utilisateur> recherche(String data){
        List<Utilisateur> utilisateurs = new ArrayList<>() ; // Initialize the ObservableList
        String req = "SELECT * FROM `utilisateurs` WHERE pseudo=? OR cin =? OR nom =? OR prenom =? OR age =? OR numTel =? OR email =?";
        try {
            PreparedStatement ps= cnx.prepareStatement(req);
            ps.setString(1, data);
            ps.setString(2, data);
            ps.setString(3, data);
            ps.setString(4, data);
            ps.setString(5, data);
            ps.setString(6, data);
            ps.setString(7, data);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Utilisateur u = new Utilisateur();
                u.setPseudo(rs.getString("PSEUDO"));
                u.setCin(rs.getInt("CIN"));
                u.setNom(rs.getString("NOM"));
                u.setPrenom(rs.getString("PRENOM"));
                u.setAge(rs.getInt("AGE"));
                u.setNumtel(rs.getInt("NUMTEL"));
                u.setEmail(rs.getString("EMAIL"));
                u.setMdp(rs.getString("MDP"));
                u.setRole(rs.getString("ROLE"));
                utilisateurs.add(u);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return utilisateurs;
    }
    public void modifierMdp(Utilisateur utilisateur, String mdp) throws SQLException {
        String req="UPDATE `utilisateurs` SET `mdp`=? WHERE pseudo=?";
        PreparedStatement ps= cnx.prepareStatement(req);
        ps.setString(1, mdp);
        ps.setString(2, utilisateur.getPseudo());
        ps.executeUpdate();
        System.out.println("Personne modifie");
    }



}
