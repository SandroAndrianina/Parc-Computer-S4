package models;

public class Ordinateur {
    private int idOrdinateur;
    private int idModele;
    private int ram;
    private String processeur;
    private int disqueDur;
    private String libelleModele;
    private String libelleMarque;
    private String reference;

    public Ordinateur() {

    }

    public Ordinateur(int idModele, int ram, String processeur, int disqueDur) {
        this.idModele = idModele;
        this.ram = ram;
        this.processeur = processeur;
        this.disqueDur = disqueDur;
    }

    public int getIdOrdinateur() {
        return idOrdinateur;
    }

    public void setIdOrdinateur(int idOrdinateur) {
        this.idOrdinateur = idOrdinateur;
    }

    public int getIdModele() {
        return idModele;
    }

    public void setIdModele(int idModele) {
        this.idModele = idModele;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getProcesseur() {
        return processeur;
    }

    public void setProcesseur(String processeur) {
        this.processeur = processeur;
    }

    public int getDisqueDur() {
        return disqueDur;
    }

    public void setDisqueDur(int disqueDur) {
        this.disqueDur = disqueDur;
    }

    public String getLibelleModele() {
        return libelleModele;
    }

    public void setLibelleModele(String libelleModele) {
        this.libelleModele = libelleModele;
    }

    public String getLibelleMarque() {
        return libelleMarque;
    }

    public void setLibelleMarque(String libelleMarque) {
        this.libelleMarque = libelleMarque;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}