package services.store;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    public  void ajouter(T t) throws SQLException;
    public  void modifier(T t) throws SQLException;
    public  void supprimer(String ref) throws SQLException;




    public List<T> afficher() throws SQLException;



}
