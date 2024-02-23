package entities;

public class Reservation {
    private int id ,prix ,terrainId;
    private String clientPseudo ,equipements ,date ,duree;


    public Reservation() {
    }

    public Reservation(int id, int prix, int terrainid, String clientPseudo, String equipements, String date, String duree) {
        this.id = id;
        this.prix = prix;
        this.terrainId = terrainid;
        this.clientPseudo = clientPseudo;
        this.equipements = equipements;
        this.date = date;
        this.duree = duree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getTerrainid() {
        return terrainId;
    }

    public void setTerrainid(int terrainid) {
        this.terrainId= terrainid;
    }

    public String getClientPseudo() {
        return clientPseudo;
    }

    public void setClientPseudo(String clientPseudo) {
        this.clientPseudo = clientPseudo;
    }

    public String getEquipements() {
        return equipements;
    }

    public void setEquipements(String equipements) {
        this.equipements = equipements;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }


    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", prix=" + prix +
                ", terrainid=" + terrainId +
                ", clientPseudo='" + clientPseudo + '\'' +
                ", equipements='" + equipements + '\'' +
                ", date='" + date + '\'' +
                ", duree='" + duree + '\'' +
                '}';
    }
}
