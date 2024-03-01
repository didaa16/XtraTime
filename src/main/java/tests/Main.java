package tests;
import entities.complexe;
import entities.terrain;
import utils.MyDatabase;
import services.ServiceComplexe;
import services.ServiceTerrain;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ServiceComplexe serviceComplexe= new ServiceComplexe();
        complexe p1= new complexe("ahmed","dida16","aa","bb","cc","dd");
        complexe p2= new complexe("yassin","dida16","mm","sqa","kjh","mpo");
       complexe p3= new complexe("adam","bohmid","zaza","sd","njk","lkm");
        ServiceTerrain serviceTerrain= new ServiceTerrain();
        //terrain t1= new terrain(2,"ahmed",11,"aa",100,"cc","dd");
       // terrain t2= new terrain(3,"yassin",12,"mm",200,"kjh","mpo");
      //  terrain t3= new terrain(6,"lolo",13,"zaza",300,"njk","lkm");
        try {
           //serviceComplexe.supprimer(1);
             //serviceComplexe.ajouter(p1);
            //serviceComplexe.ajouter(p2);
            //  serviceComplexe.ajouter(p3);
             serviceComplexe.modifier(p1);
            // System.out.println(serviceComplexe.afficher());
            //serviceTerrain.ajouter(t1);
        // serviceTerrain.ajouter(t2);
          // serviceTerrain.ajouter(t3);
           // serviceTerrain.supprimer(3);
            System.out.println(serviceTerrain.afficher());
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}