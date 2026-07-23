package models;

public class KPIDTO {
    private int totalOrdinateurs;
    private int totalFonctionnels;
    private int totalDysfonctionnels;
    private int totalAlimentation;
    private int totalDisqueDur;
    private int totalCarteMere;
    private String date;


    public KPIDTO(int totalOrdinateurs, int totalFonctionnels, int totalDysfonctionnels,
                            int totalAlimentation, int totalDisqueDur, int totalCarteMere, String date) {
        this.totalOrdinateurs = totalOrdinateurs;
        this.totalFonctionnels = totalFonctionnels;
        this.totalDysfonctionnels = totalDysfonctionnels;
        // panne
        this.totalAlimentation = totalAlimentation;
        this.totalDisqueDur = totalDisqueDur;
        this.totalCarteMere = totalCarteMere;

        this.date = date;
    }
}