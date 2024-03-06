package services.local;

import entities.local.terrain;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceTerrain  implements  Iservice<terrain>{
    ServiceComplexe serviceComplexe = new ServiceComplexe();
    Connection connection;
    public ServiceTerrain(){
        connection= MyDataBase.getInstance().getConnection();

    }
@Override
    public void ajouter(terrain terrain) throws SQLException {
        int lastRef = serviceComplexe.getLastRef();
        if (lastRef == 0) {
            throw new SQLException("Aucune référence trouvée dans la table complexe.");
        }

        String req = "INSERT INTO terrain (ref, nom, capacite, type, prix, disponibilite, img) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(req);

        ps.setInt(1, lastRef); // Utilise la dernière référence de la table complexe
        ps.setString(2, terrain.getNom());
        ps.setInt(3, terrain.getCapacite());
        ps.setString(4, terrain.getType());
        ps.setInt(5, terrain.getPrix());
        ps.setString(6, terrain.getDisponibilite());
        ps.setString(7, terrain.getImg());

        ps.executeUpdate();

        System.out.println("Terrain ajouté avec succès !");
    }

/*
   @Override
    public void ajouter(terrain terrain) throws SQLException {
        String req ="insert into terrain (ref,nom,capacite,type,prix,disponibilite,img)"+
                "values("+terrain.getRef()+",'"+terrain.getNom()+"',"+terrain.getCapacite()+",'"+terrain.getType()+"',"+terrain.getPrix()+",'"+terrain.getDisponibilite()+"','"+terrain.getImg()+"')";
        Statement st = connection.createStatement();
        st.executeUpdate(req);
        System.out.println("terrain ajouté");
    }


*/
    @Override
    public void modifier(terrain terrain) throws SQLException {
        String req="update terrain set ref=? ,nom=? , capacite=?,type=?,prix=?,disponibilite=?, img=? where id=?";
        PreparedStatement ps= connection.prepareStatement(req);

        ps.setInt(1, terrain.getRef());
        ps.setString(2, terrain.getNom());
        ps.setInt(3, terrain.getCapacite());
        ps.setString(4, terrain.getType());
        ps.setInt(5, terrain.getPrix());
        ps.setString(6, terrain.getDisponibilite());
        ps.setString(7, terrain.getImg());
        ps.setInt(8, terrain.getId());
        ps.executeUpdate();
        System.out.println("terrain modifie");

    }



    @Override
    public List<terrain> afficher() throws SQLException {

        List<terrain> terrains= new ArrayList<>();
        String req="select * from terrain ";
        Statement st  = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()){
            terrain p = new terrain();
            p.setId(rs.getInt(1));
            p.setRef(rs.getInt(2));
            p.setNom(rs.getString("nom"));
            p.setCapacite(rs.getInt(4));
            p.setType(rs.getString(5));
            p.setPrix(rs.getInt(6));
            p.setDisponibilite(rs.getString(7));
            p.setImg(rs.getString(8));
            terrains.add(p);
        }
        return terrains;
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String requete = "DELETE FROM terrain WHERE id="+id;
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(requete);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}