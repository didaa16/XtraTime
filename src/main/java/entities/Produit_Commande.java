package entities;

public class Produit_Commande {

    private String ref;
    private int refCommande;
    private int nbr ;


    public Produit_Commande() {}

    public Produit_Commande(String ref, int refCommande,int nbr ) {
        this.ref = ref;
        this.refCommande = refCommande;
        this.nbr  = nbr ;
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

    public int getNbr() {
        return nbr ;
    }

    public void setNbr (int nbr ) {
        this.nbr  = nbr ;
    }

    @Override
    public String toString() {
        return "Produit_Commande{" +
                "ref='" + ref + '\'' +
                ", refCommande=" + refCommande +
                ", nbr =" + nbr  +
                '}';
    }
}
