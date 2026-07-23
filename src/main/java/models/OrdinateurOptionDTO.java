package models;

public class OrdinateurOptionDTO {
    private int idOrdinateur;
    private String libelle;      // ex: "Dell Latitude - Intel i5"
    private String derniereDate; // null si aucun historique pour cet ordinateur

    public OrdinateurOptionDTO(int idOrdinateur, String libelle, String derniereDate) {
        this.idOrdinateur = idOrdinateur;
        this.libelle = libelle;
        this.derniereDate = derniereDate;
    }
}