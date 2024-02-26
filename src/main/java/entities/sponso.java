package entities;

public class sponso {
    private int idsponso;
    private String nom;
    private int tel;

    private String email;
    private String image;


    public sponso() {
    }

    public sponso(int idsponso, String nom, int tel, String email, String image) {
        this.idsponso = idsponso;
        this.nom = nom;
        this.tel = tel;
        this.email = email;
        this.image = image;
    }

    public sponso(String nom, int tel, String email, String image) {
        this.nom = nom;
        this.tel = tel;
        this.email = email;
        this.image = image;
    }

    public int getIdsponso() {
        return idsponso;
    }

    public void setIdsponso(int idsponso) {
        this.idsponso = idsponso;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "sponso{" +
                "idsponso=" + idsponso +
                ", nom='" + nom + '\'' +
                ", tel=" + tel +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
