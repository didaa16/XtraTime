package services.utilisateur;

import entities.utilisateur.Img;
import services.IService;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceImg implements IService<Img> {
    private Connection cnx;
    private Statement st;
    private PreparedStatement pst;

    public ServiceImg() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Img img) throws SQLException {
        String req = "INSERT INTO `pdp`(`pseudoU`, `image`) VALUES (?,?)";
        PreparedStatement ps= cnx.prepareStatement(req);
        ps.setString(1, img.getPseudoU());
        ps.setString(2, img.getImg());
        ps.executeUpdate();
        System.out.println("Image Ajoutée");
    }

    @Override
    public void supprimer(Img img) throws SQLException {
        String req = "DELETE FROM `pdp` WHERE pseudoU=?";
        PreparedStatement ps= cnx.prepareStatement(req);
        ps.setString(1, img.getPseudoU());
        ps.executeUpdate();
        System.out.println("Image Supprimée");
    }

    @Override
    public void modifier(Img img) throws SQLException {
        String req="UPDATE `pdp` SET `image`=? WHERE `pseudoU` = ?";
        PreparedStatement ps= cnx.prepareStatement(req);
        ps.setString(1, img.getImg());
        ps.setString(2, img.getPseudoU());
        ps.executeUpdate();
        System.out.println("Personne modifie");
    }

    @Override
    public List<Img> afficher() {
        List<Img> imgs= new ArrayList<>();
        String req="SELECT * FROM `pdp`";
        try {
            Statement st  = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()){
                Img i = new Img();
                i.setPseudoU(rs.getString("pseudoU"));
                i.setImg(rs.getString("image"));
                imgs.add(i);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return imgs;
    }
    public Img afficherImageParPseudo(String pseudoU) throws SQLException {
        Img i = new Img();
        String req = "SELECT * FROM `pdp` WHERE `pseudoU` = ?";
        PreparedStatement ps= cnx.prepareStatement(req);
        ps.setString(1, pseudoU);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            i = new Img(rs.getString("pseudoU"),rs.getString("image"));
        }
        return i;
    }

    public boolean imgExiste(String pseudoU) throws SQLException {
        String req = "SELECT * FROM `pdp` WHERE `pseudoU` = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, pseudoU);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

}