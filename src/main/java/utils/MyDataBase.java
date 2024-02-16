package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private Connection cnx;
    private String url ="jdbc:mysql://localhost:3306/user";
    private String login = "root";
    private String pwd ="";
    private static MyDataBase instance;

    private MyDataBase() {
        try {
            cnx = DriverManager.getConnection(url,login,pwd);
            System.out.println("SUCCESS");
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static MyDataBase getInstance(){
        if (instance ==null)
            instance=new MyDataBase();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}