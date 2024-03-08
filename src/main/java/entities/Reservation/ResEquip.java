package entities.Reservation;

public class ResEquip {
    private int idReservation;
    private int idEquipement;

    public ResEquip() {
    }

    public ResEquip(int idReservation, int idEquipement) {
        this.idReservation = idReservation;
        this.idEquipement = idEquipement;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdEquipement() {
        return idEquipement;
    }

    public void setIdEquipement(int idEquipement) {
        this.idEquipement = idEquipement;
    }

    @Override
    public String toString() {
        return "ReservationEquipement{" +
                "idReservation=" + idReservation +
                ", idEquipement=" + idEquipement +
                '}';
    }
}
