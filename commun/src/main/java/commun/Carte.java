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

    /**
     * Constructeur de la classe Carte
     * @param nomCarte nom de la carte
     * @param points nombre de points que la carte rapporte, si définit à 0, est set à 1
     */
    public Carte(){}

    public Carte(String nomCarte, int points){
        this.nomCarte = nomCarte;
        if (points == 0){ this.pointsCarte = 1; }
        else{ this.pointsCarte = points; }
    }

    /*---------- Geteurs ----------*/
    public String getNomCarte() { return nomCarte; }
    public int getPointsCarte() { return pointsCarte; }
}
