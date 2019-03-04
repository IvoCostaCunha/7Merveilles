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
     * @param points nombre de points que la carte rapporte
     * @param prix Prix de la carte
     */
    public Carte(){}

    public Carte(String nomCarte, int points) {
        this.nomCarte = nomCarte;
        this.pointsCarte = points;
    }

    public Carte(String nomCarte, int points, int prix){
        this.nomCarte = nomCarte;
        this.pointsCarte = points;
        this.prixCarte = prix;
    }

    /*---------- Geteurs ----------*/
    public String getNomCarte() { return nomCarte; }
    public int getPointsCarte() { return pointsCarte; }
    public int getPrixCarte() {return prixCarte; }
}
