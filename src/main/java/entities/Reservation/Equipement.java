package entities.Reservation;

import jdk.jfr.MemoryAddress;

public class Equipement {
    private int idEquipement ,stock ,prix , terrainIdAjout;
    private String nom ,description ,type ,image;

    public Equipement() {
    }

    public Equipement(int idEquipement,String nom, String description, String type, int prix, String image, int stock , int terrainIdAjout) {
        this.idEquipement = idEquipement;
        this.stock = stock;
        this.prix = prix;
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.image = image;
        this.terrainIdAjout = terrainIdAjout ;
    }

    public Equipement(String nom, String description, String type, int prix, String image, int stock , int terrainIdAjout) {
        this.stock = stock;
        this.prix = prix;
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.image = image;
        this.terrainIdAjout = terrainIdAjout;
    }


    public int getTerrainIdAjout() {
        return terrainIdAjout;
    }

    public void setTerrainIdAjout(int terrainIdAjout) {
        this.terrainIdAjout = terrainIdAjout;
    }

    public int getId() {
        return idEquipement;
    }

    public void setId(int id) {
        this.idEquipement = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Equipement{" +
                "id=" + idEquipement +
                ", stock=" + stock +
                ", prix=" + prix +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
