package services.local;

import entities.local.complexe;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceComplexe implements  Iservice<complexe>{
    Connection connection;
    public ServiceComplexe(){
        connection= MyDatabase.getInstance().getConnection();

    }
    @Override
    public void ajouter(complexe complexe) throws SQLException {
        String req ="insert into complexe (nom,idlocateur,adresse,tel,patente,image)" + "values('"+complexe.getNom()+"','"+complexe.getIdlocateur()+"','"+complexe.getAdresse()+"','"+complexe.getTel()+"','"+complexe.getPatente()+"','"+complexe.getImage()+"')";
        Statement st = connection.createStatement();
        st.executeUpdate(req);
        System.out.println("complexe ajouté");
    }


    public int getLastRef() throws SQLException {
        String req = "SELECT MAX(ref) FROM complexe";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0; // retourne 0 si la table complexe est vide
    }
    @Override
    public void modifier(complexe complexe) throws SQLException {
        String req="update complexe set nom=? ,idlocateur=? , adresse=?,tel=?,patente=?,image=? where ref=?";
        PreparedStatement ps= connection.prepareStatement(req);

        ps.setString(1, complexe.getNom());
        ps.setString(2, complexe.getIdlocateur());
        ps.setString(3, complexe.getAdresse());
        ps.setString(4, complexe.getTel());
        ps.setString(5, complexe.getPatente());
        ps.setString(6, complexe.getImage());
        ps.setInt(7, complexe.getRef());
        ps.executeUpdate();
        System.out.println("complexe modifie");

    }



    @Override
    public List<complexe> afficher( ) throws SQLException {

        List<complexe> complexes= new ArrayList<>();
        String req="select * from complexe";
        Statement st  = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()){
            complexe p = new complexe();
            p.setRef(rs.getInt(1));
            p.setIdlocateur(rs.getString(3));
            p.setNom(rs.getString("nom"));
            p.setAdresse(rs.getString(4));
            p.setTel(rs.getString(5));
            p.setPatente(rs.getString(6));
            p.setImage(rs.getString(7));
            complexes.add(p);
        }
        return complexes;
    }

    @Override
    public void supprimer(int ref) throws SQLException {
        String requete = "DELETE FROM complexe WHERE ref="+ref;
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(requete);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
