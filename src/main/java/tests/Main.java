package tests;

import entities.*;
import services.ServiceCommande;
import services.ServiceCommandeProduit;
import services.ServiceProduit;
import utils.DataBase;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {

        ServiceCommandeProduit serviceCommandeProduit = new ServiceCommandeProduit();
        ServiceProduit serviceProduit = new ServiceProduit();


        Produit produit = new Produit("refProduit", "nom", "description", 0.0, Marque.adidas, "taille", TypeSport.football, 0, "image");
        //serviceProduit.ajouter(produit);
           // serviceProduit.supprimer("refProduit");

            ServiceCommande serviceCommande = new ServiceCommande();
            Commande commande = new Commande(0.0, Status.enAttente, "boh");
            Commande commande2 = new Commande(0.0, Status.enAttente, "boh");


        // Ajouter le produit à la commande
        //   serviceCommandeProduit.ajouterProduitACommande(2,"RF-44");
         //  serviceCommandeProduit.ajouterProduitACommande(1,"RF-123");
            System.out.println(serviceCommandeProduit.listerP("commande1"));
          //  serviceCommande.ajouter(commande);
          //  serviceCommande.ajouter(commande2);
    } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
