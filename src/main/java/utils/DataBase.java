package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

    final String URL="jdbc:mysql://localhost:3306/xtratime";
    final String USERNAME="root";
    final String PASSWORD="";
    static DataBase instance;
    Connection connection;
    private DataBase(){
        try {
            connection= DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("Connexion etablie");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public  static DataBase getInstance(){
        if(instance==null){
            instance= new DataBase();
        }
        return instance;

    }

    public Connection getConnection() {
        return connection;
    }
}
