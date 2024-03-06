package entities.local;

public class terrain {
    private 	int id,	ref	, capacite		, prix ;
         private String   nom ,type,disponibilite, img ;

         public terrain()
         {

         }

    public terrain(int id, int ref, String nom, int capacite,  String type,int prix, String disponibilite, String img) {
        this.id = id;
        this.ref = ref;
        this.nom = nom;
        this.capacite = capacite;
        this.type = type;
        this.prix = prix;
        this.disponibilite = disponibilite;
        this.img = img;
    }

    public terrain(String nom, int capacite,  String type,int prix, String disponibilte, String img) {

        // this.ref = ref;
        this.nom = nom;
        this.capacite = capacite;
        this.type = type;
        this.prix = prix;
        this.disponibilite = disponibilte;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "terrain{" +
                "id=" + id +
                ", ref=" + ref +
                ", capacite=" + capacite +
                ", prix=" + prix +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", disponibilite='" + disponibilite + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
