package services;
import java.sql.SQLException;
import java.util.List;
public interface IService<A> {
    public void ajouter(A a) throws SQLException;

    public void modifier(A a) throws SQLException;

    public void supprimer(int id) throws SQLException;

    public List<A> afficher() throws SQLException;

}