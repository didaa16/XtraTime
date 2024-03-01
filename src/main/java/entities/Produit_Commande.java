package entities;

public class Produit_Commande {

    private String ref;
    private int refCommande;


    public Produit_Commande() {}

    public Produit_Commande(String ref, int refCommande) {
        this.ref = ref;
        this.refCommande = refCommande;
    }


    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public int getRefCommande() {
        return refCommande;
    }

    public void setRefCommande(int refCommande) {
        this.refCommande = refCommande;
    }

    @Override
    public String toString() {
        return "Produit_Commande{" +
                ", ref='" + ref + '\'' +
                ", refCommande='" + refCommande + '\'' +
                '}';
    }
}
