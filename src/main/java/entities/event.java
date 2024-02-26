package entities;
import java.sql.Timestamp;
import java.time.LocalDate;

public class event {
    private int idevent;
    private String titre;
    private String description;
    private String image;
    private Timestamp datedebut;
    private Timestamp datefin;

    private int idterrain;
    private int idsponso;
    private String iduser;

    public event() {
    }

    public event(int idevent, String titre, String description, String image,
                 Timestamp datedebut, Timestamp datefin, int idterrain, int idsponso, String iduser) {
        this.idevent = idevent;
        this.titre = titre;
        this.description = description;
        this.image = image;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.idterrain = idterrain;
        this.idsponso = idsponso;
        this.iduser = iduser;
    }

    public event(String titre, String description, String image,
                 Timestamp datedebut, Timestamp datefin, int idterrain, int idsponso, String iduser) {
        this.titre = titre;
        this.description = description;
        this.image = image;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.idterrain = idterrain;
        this.idsponso = idsponso;
        this.iduser = iduser;
    }

    public event(int idevent, String titre, String description, String image, Timestamp datedebut, Timestamp datefin, int idterrain, String sponsorName, String iduser) {
        this.titre = titre;
        this.description = description;
        this.image = image;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.idterrain = idterrain;
        
        this.iduser = iduser;
    }


    public int getIdevent() {
        return idevent;
    }

    public void setIdevent(int idevent) {
        this.idevent = idevent;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
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

    public Timestamp getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(Timestamp datedebut) {
        this.datedebut = datedebut;
    }

    public Timestamp getDatefin() {
        return datefin;
    }

    public void setDatefin(Timestamp datefin) {
        this.datefin = datefin;
    }

    public int getIdterrain() {
        return idterrain;
    }

    public void setIdterrain(int idterrain) {
        this.idterrain = idterrain;
    }

    public int getIdsponso() {
        return idsponso;
    }

    public void setIdsponso(int idsponso) {
        this.idsponso = idsponso;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    @Override
    public String toString() {
        return "event{" +
                "idevent=" + idevent +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", datedebut=" + datedebut +
                ", datefin=" + datefin +
                ", idterrain=" + idterrain +
                ", idsponso=" + idsponso +
                ", iduser='" + iduser + '\'' +
                '}';
    }


}
