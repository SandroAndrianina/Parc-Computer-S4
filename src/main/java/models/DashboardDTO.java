package models;

import java.util.List;

public class DashboardDTO {
    private int totalOrdinateurs;
    private int totalFonctionnels;
    private int totalDysfonctionnels;
    private List<EvolutionPanneDTO> evolutionPannes;

    public DashboardDTO(int totalOrdinateurs, int totalFonctionnels, int totalDysfonctionnels,
                         List<EvolutionPanneDTO> evolutionPannes) {
        this.totalOrdinateurs = totalOrdinateurs;
        this.totalFonctionnels = totalFonctionnels;
        this.totalDysfonctionnels = totalDysfonctionnels;
        this.evolutionPannes = evolutionPannes;
    }
}