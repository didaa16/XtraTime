package entities;

import java.util.Objects;

public class Img {
    private String pseudoU;
    private String img;

    public Img() {
    }

    public Img(String img) {
        this.img = img;
    }

    public Img(String pseudoU, String img) {
        this.pseudoU = pseudoU;
        this.img = img;
    }

    public String getPseudoU() {
        return pseudoU;
    }

    public void setPseudoU(String pseudoU) {
        this.pseudoU = pseudoU;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Img{" +
                "pseudoU='" + pseudoU + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Img img1)) return false;
        return Objects.equals(getPseudoU(), img1.getPseudoU()) && Objects.equals(getImg(), img1.getImg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPseudoU(), getImg());
    }
}
