package commun;

/**
 * @author Ivo COSTA CUNHA on 15/02/2019
 * @project 7Merveilles
 */

/**
 * Classe qui représente les cartes
 */
public class Carte {
    private String nomCarte;
    private int pointsCarte;
    private int prixCarte;

    /**
     * Constructeur de la classe Carte
     * @param nomCarte nom de la carte
<<<<<<< HEAD
     * @param points nombre de points que la carte rapporte
     * @param prix Prix de la carte
=======
     * @param points nombre de points que la carte rapporte, si définit à 0, est set à 1
>>>>>>> 0d776f92c1405bb4ff94ec5124e5e0f651307e9d
     */
    public Carte(){}

    public Carte(String nomCarte, int points) {
        this.nomCarte = nomCarte;
        if (points == 0){ this.pointsCarte = 1; }
        else{ this.pointsCarte = points; }
    }

    public Carte(String nomCarte, int points, int prix){
        this.nomCarte = nomCarte;
        if (points == 0){ this.pointsCarte = 1; }
        else{ this.pointsCarte = points; }
        this.prixCarte = prix;
    }

    /*---------- Geteurs ----------*/
    public String getNomCarte() { return nomCarte; }
    public int getPointsCarte() { return pointsCarte; }
    public int getPrixCarte() {return prixCarte; }
}
