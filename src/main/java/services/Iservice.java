package services;
import entities.complexe;
import entities.terrain;
import java.sql.SQLException;
import java.util.List;
public interface Iservice<T> {

    public  void ajouter(T t) throws SQLException;
    public  void modifier(T t) throws SQLException;
    public  void supprimer(int id) throws SQLException;

    public List<T> afficher( ) throws SQLException;


}
