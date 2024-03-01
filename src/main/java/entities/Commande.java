package entities;

import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int refCommande;
    private double prix;
    private Status status;
    private String idUser;

    private List<Produit> produits = new ArrayList<>();

    public Commande() {
    }
    public Commande(double prix, Status status, String idUser) {
        this.prix = prix;
        this.status = status;
        this.idUser = idUser;
    }
    public Commande(int refCommande) {
        this.refCommande = refCommande;
    }

    public Commande(int refCommande, double prix, Status status, String idUser) {
        this.refCommande = refCommande;
        this.prix = prix;
        this.status = status;
        this.idUser = idUser;
        this.produits = produits;
    }


    public int getRefCommande() {
        return refCommande;
    }

    public void setRefCommande(int refCommande) {
        this.refCommande = refCommande;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }


    public void addProduit(Produit produit) {
        produits.add(produit);
    }

    @Override
    public String toString() {
        return "Commande{" +
                "refCommande='" + refCommande + '\'' +
                ", prix=" + prix +
                ", status=" + status +
                ", idUser='" + idUser + '\'' +
                ", produits=" + produits +
                '}';
    }
}
