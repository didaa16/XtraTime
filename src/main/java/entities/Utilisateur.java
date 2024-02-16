package entities;

import java.util.Objects;

public class Utilisateur {
    private String pseudo, nom, prenom, email, mdp, role ;
    private int cin, age, numtel;

    public Utilisateur() {
    }

    public Utilisateur(String pseudo, int cin, String nom, String prenom, int age, int numtel, String email, String mdp, String role) {
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
        this.cin = cin;
        this.numtel = numtel;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public int getNumtel() {
        return numtel;
    }

    public void setNumtel(int numtel) {
        this.numtel = numtel;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "pseudo='" + pseudo + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                ", role='" + role + '\'' +
                ", cin=" + cin +
                ", age=" + age +
                ", numtel=" + numtel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur that)) return false;
        return Objects.equals(getPseudo(), that.getPseudo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPseudo());
    }
}
