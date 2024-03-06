package entities.Abonnement;

import java.util.Date;

public class Abonnement {

    private int id;
    private Date date;
    private float prix;
    private float prixTotal;
    private int terrainId;
    private int packId;
    private String nomUser;
    private String nomTerrain ;
    private int numtel;
    private String nomPack; // Nouvel attribut nomPack

    public Abonnement() {
    }

    public Abonnement(int id, Date date, float prix, float prixTotal, int terrainId, int packId, String nomUser, String nomTerrain, int numtel, String nomPack) {
        this.id = id;
        this.date = date;
        this.prix = prix;
        this.prixTotal = prixTotal;
        this.terrainId = terrainId;
        this.packId = packId;
        this.nomUser = nomUser;
        this.nomTerrain = nomTerrain;
        this.numtel = numtel;
        this.nomPack = nomPack;
    }

    public Abonnement(Date date, float prix, String nomUser, String nomTerrain, int numtel, String nomPack) {
        this.date = date;
        this.prix = prix;
        this.nomUser = nomUser;
        this.nomTerrain = nomTerrain;
        this.numtel = numtel;
        this.nomPack = nomPack;
    }

    public Abonnement(int id, Date date, float prix, String nomUser, String nomTerrain, int numtel, String nomPack) {
        this.id = id;
        this.date = date;
        this.prix = prix;
        this.nomUser = nomUser;
        this.nomTerrain = nomTerrain ;
        this.numtel = numtel;
        this.nomPack = nomPack;
    }


    public float getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(float prixTotal) {
        this.prixTotal = prixTotal;
    }

    public int getTerrainId() {
        return terrainId;
    }

    public void setTerrainId(int terrainId) {
        this.terrainId = terrainId;
    }

    public int getPackId() {
        return packId;
    }

    public void setPackId(int packId) {
        this.packId = packId;
    }

    public String getNomTerrain() {
        return nomTerrain;
    }

    public void setNomTerrain(String nomTerrain) {
        this.nomTerrain = nomTerrain;
    }

    // Getters et setters pour id, date, prix, terraindId, pack_id, clientPseudo, image et nomPack
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }



    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public int getNumtel() {
        return numtel;
    }

    public void setNumtel(int numtel) {
        this.numtel = numtel;
    }

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "id=" + id +
                ", date=" + date +
                ", prix=" + prix +
                ", prixTotal=" + prixTotal +
                ", terrainId=" + terrainId +
                ", packId=" + packId +
                ", nomUser='" + nomUser + '\'' +
                ", nomTerrain='" + nomTerrain + '\'' +
                ", numtel=" + numtel +
                ", nomPack='" + nomPack + '\'' +
                '}';
    }
}
