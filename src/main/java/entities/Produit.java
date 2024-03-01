package entities;

public class Produit {
    private String ref;
    private String nom;
    private String description;
    private double prix;
    private Marque marque;
    private String taille;
    private TypeSport typeSport;
    private int quantite;
    private String image;


    public Produit() {
    }

    public Produit(String ref, String nom, String description, double prix, Marque marque, String taille, TypeSport typeSport, int quantite, String image) {
        this.ref = ref;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.marque = marque;
        this.taille = taille;
        this.typeSport = typeSport;
        this.quantite = quantite;
        this.image =image;
    }

    public Produit(String text, String text1, String text2, double prix, Object value, String text3, String value1, int quantite, String text4) {
    }


    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Marque getMarque() {
        return marque;
    }

    public void setMarque(Marque marque) {
        this.marque = marque;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public TypeSport getTypeSport() {
        return typeSport;
    }

    public void setTypeSport(TypeSport typeSport) {
        this.typeSport = typeSport;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "ref='" + ref + '\'' +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", marque=" + marque +
                ", taille='" + taille + '\'' +
                ", typeSport=" + typeSport +
                ", quantite=" + quantite +
                ", image='" + image + '\'' +
                '}';
    }

    public void updateRating(String ref, double newRating) {
    }
}
