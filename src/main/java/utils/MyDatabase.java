package utils;

<<<<<<< HEAD


=======
>>>>>>> local
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
<<<<<<< HEAD

    final String URL="jdbc:mysql://localhost:3306/xtratime";
    final String USERNAME="root";
    final String PASSWORD="";
    static MyDatabase instance;
    Connection connection;
    public MyDatabase(){
        try {
            connection= DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("Connexion etablie");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public  static MyDatabase getInstance(){
        if(instance==null){
            instance= new MyDatabase();
        }
        return instance;

    }
=======
    final  String URL="jdbc:mysql://localhost:3306/xtratime";
    final  String USERNAME="root";
    final  String PASSWORD="";
     static  MyDatabase instance;
    Connection connection ;
    private  MyDatabase (){
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("connexion done ");

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static  MyDatabase getInstance(){
        if(instance==null ){
            instance=new MyDatabase();
        }
return  instance;
    }
>>>>>>> local

    public Connection getConnection() {
        return connection;
    }
}
