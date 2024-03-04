package entities;

public class Produit_Commande {

    private String ref;
    private int refCommande;
    private int quantitProd;


    public Produit_Commande() {}

    public Produit_Commande(String ref, int refCommande,int quantitProd) {
        this.ref = ref;
        this.refCommande = refCommande;
        this.quantitProd = quantitProd;
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

    public int getQuantitProd() {
        return quantitProd;
    }

    public void setQuantitProd(int quantitProd) {
        this.quantitProd = quantitProd;
    }

    @Override
    public String toString() {
        return "Produit_Commande{" +
                "ref='" + ref + '\'' +
                ", refCommande=" + refCommande +
                ", quantitProd=" + quantitProd +
                '}';
    }
}
