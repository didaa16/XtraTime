package entities.local;

public class complexe {
    private int ref;
    private String nom ,adresse,tel,patente,image, idlocateur;

    public complexe() {
    }

    public complexe(int ref, String nom,String idlocateur, String adresse,String tel,String patente,String image) {
        this.ref = ref;
        this.nom = nom;
        this.idlocateur = idlocateur;

        this.adresse = adresse;
        this.tel = tel;
        this.patente = patente;
        this.image = image;
    }

    public complexe(String nom, String idlocateur, String adresse, String tel, String patente, String image) {
        this.nom = nom;
        this.idlocateur = idlocateur;

        this.adresse = adresse;
        this.tel = tel;
        this.patente = patente;
        this.image = image;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIdlocateur() {
        return idlocateur;
    }

    public void setIdlocateur(String idlocateur) {
        this.idlocateur = idlocateur;
    }


    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "complexe{" +
                "ref=" + ref +
                ", idlocateur=" + idlocateur +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", tel='" + tel + '\'' +
                ", patente='" + patente + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
