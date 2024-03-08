package entities.Reservation;

public class Reservation {
    private int id;
    private int prix;
    private int terrainId;
    private String clientPseudo;
    private String equipements;
    private String date;
    private String duree;



    public Reservation(String equipements, String date, String duree) {
        this.equipements = equipements;
        this.date = date;
        this.duree = duree;
    }

    public Reservation(int id, String date, String duree, int prix, int terrainId, String clientPseudo, String equipements) {
        this.id = id;
        this.prix = prix;
        this.terrainId = terrainId;
        this.clientPseudo = clientPseudo;
        this.equipements = equipements;
        this.date = date;
        this.duree = duree;
    }

    public Reservation(int prix, int terrainId, String clientPseudo, String equipements, String date, String duree) {
        this.id = id;
        this.prix = prix;
        this.terrainId = terrainId;
        this.clientPseudo = clientPseudo;
        this.equipements = equipements;
        this.date = date;
        this.duree = duree;
    }

    public Reservation() {

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

    public int getTerrainId() {
        return terrainId;
    }

    public void setTerrainId(int terrainId) {
        this.terrainId = terrainId;
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
                ", terrainId=" + terrainId +
                ", clientPseudo='" + clientPseudo + '\'' +
                ", equipements='" + equipements + '\'' +
                ", date='" + date + '\'' +
                ", duree=" + duree +
                '}';
    }

}