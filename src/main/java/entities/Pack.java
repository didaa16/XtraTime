package entities;

public class Pack {
    private int idP;
    private String nom, description, image; // Ajout de l'attribut image
    private int reduction;

    public Pack() {
    }

    public Pack(int idP, String nom, String description, String image, int reduction) {
        this.idP = idP;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.reduction = reduction;
    }

    public Pack(String nom, String description, String image, int reduction) {
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.reduction = reduction;
    }

    public int getIdP() {
        return idP;
    }

    public void setIdP(int idP) {
        this.idP = idP;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getReduction() {
        return reduction;
    }

    public void setReduction(int reduction) {
        this.reduction = reduction;
    }

    @Override
    public String toString() {
        return "Pack{" +
                "idP=" + idP +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", reduction=" + reduction +
                '}';
    }
}
