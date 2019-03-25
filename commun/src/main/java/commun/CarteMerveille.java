package commun;

public class CarteMerveille {
    String nomCarteMerveille;
    int numPlateau;

    public CarteMerveille(String nom, int numPlateau) {
        this.nomCarteMerveille = nom;
        this.numPlateau = numPlateau;
    }

    public String getNomCarteMerveille() {
        return nomCarteMerveille;
    }

    public void setNomCarteMerveille(String nom) {
        this.nomCarteMerveille = nom;
    }

    public int getNumPlateau() {
        return numPlateau;
    }

    public void setNumPlateau(int numPlateau) {
        this.numPlateau = numPlateau;
    }

}