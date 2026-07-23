package models;

public class HistoriqueEtatDTO {
    private int idHistorique;
    private int idOrdinateur;
    private String processeur;
    private String libelleEtat;
    private String libelleMotifsPanne;
    private String dateChangement;

    public HistoriqueEtatDTO(int idHistorique, int idOrdinateur, String processeur,
                              String libelleEtat, String libelleMotifsPanne, String dateChangement) {
        this.idHistorique = idHistorique;
        this.idOrdinateur = idOrdinateur;
        this.processeur = processeur;
        this.libelleEtat = libelleEtat;
        this.libelleMotifsPanne = libelleMotifsPanne;
        this.dateChangement = dateChangement;
    }

    // Getters (utiles si vous manipulez l'objet ailleurs, Gson n'en a pas besoin pour sérialiser)
    public int getIdHistorique() { return idHistorique; }
    public int getIdOrdinateur() { return idOrdinateur; }
    public String getProcesseur() { return processeur; }
    public String getLibelleEtat() { return libelleEtat; }
    public String getLibelleMotifsPanne() { return libelleMotifsPanne; }
    public String getDateChangement() { return dateChangement; }
}