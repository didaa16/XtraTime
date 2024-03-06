package entities.store;

public class RatingProd {
    private String ref ;
    private String idUser;
    private double rating;
    private int id;

    public RatingProd(String ref, String idUser, double rating, int id) {
        this.ref = ref;
        this.idUser = idUser;
        this.rating = rating;
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ref='" + ref + '\'' +
                ", idUser='" + idUser + '\'' +
                ", rating=" + rating +
                ", id=" + id +
                '}';
    }
}
