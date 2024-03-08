package services.Reservation;

import entities.Reservation.Equipement;
import entities.Reservation.Terrain;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceTerrain {
    Connection connection;
    public ServiceTerrain(){
        connection= MyDatabase.getInstance().getConnection();

    }
    public List<Terrain> afficher() throws SQLException {
        List<Terrain> terrains = new ArrayList<>();
        String req="select * from terrain";
        Statement st  = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()){
            Terrain t = new Terrain();
            t.setId(rs.getInt(1));
            t.setRef(rs.getInt(2));
            t.setNom(rs.getString(3));
            t.setCapacite(rs.getInt(4));
            t.setType(rs.getString(5));
            t.setPrix(rs.getInt(6));
            t.setDisp(rs.getString(7));
            t.setImage(rs.getString(8));
            terrains.add(t);
        }
        return terrains;
    }
}
