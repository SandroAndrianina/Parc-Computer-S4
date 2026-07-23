package models;
public class Modele {
    private int idModele;
    private String libelle;
    private String reference;
    private String libelleMarque;
    private int idMarque;
    
    public Modele() {}
    
    public int getIdModele() { 
        return idModele; 
    }

    public void setIdModele(int idModele) { 
        this.idModele = idModele; 
    }
    
    public String getLibelle() { 
        return libelle; 
    }

    public void setLibelle(String libelle) { 
        this.libelle = libelle; 
    }
    
    public String getReference() { 
        return reference; 
    }

    public void setReference(String reference) {
         this.reference = reference; 
    }
    
    public String getLibelleMarque() { 
        return libelleMarque; 
    }

    public void setLibelleMarque(String libelleMarque) { 
        this.libelleMarque = libelleMarque; 
    }

    public int getIdMarque() { return idMarque; }
    public void setIdMarque(int idMarque) { this.idMarque = idMarque; }
}