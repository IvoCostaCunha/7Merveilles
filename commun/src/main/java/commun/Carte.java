package commun;

/**
 * @author Ivo COSTA CUNHA on 15/02/2019
 * @project 7Merveilles
 */

import java.util.ArrayList;

/**
 * Classe qui représente les cartes
 */
public class Carte {
    private String nomCarte;
    private int pointsCarte;
    private int pointsMilitaireCarte;
    private int prixCartePieces;

    private ArrayList<Ressource> prixCarteRessources;

    /**
     * Constructeur de la carte Classe
     */
    public Carte(){}

    /**
     * Constructeur de la classe Carte, defini une carte avec un cout null
     * @param nomCarte nom de la carte
     * @param points nombre de points que la carte rapporte, si définit à 0, est set à 1
     */
    public Carte(String nomCarte, int points) {
        this.nomCarte = nomCarte;
        if (points == 0){ this.pointsCarte = 1; }
        else{ this.pointsCarte = points; }
        this.prixCartePieces = 0;
        this.prixCarteRessources = null;
    }

    /**
     * Constructeiur de la classe Carte, defini une carte avec un prix en Pieces
     * @param nomCarte nom de la carte
     * @param points nb de points que permet de gagner la carte
     * @param prixCartePieces prix de la carte en pieces
     */
    public Carte(String nomCarte, int points, int prixCartePieces){
        this.nomCarte = nomCarte;
        if (points == 0){ this.pointsCarte = 1; }
        else{ this.pointsCarte = points; }
        this.prixCartePieces = prixCartePieces;
        this.prixCarteRessources = null;
    }

    /**
     * Constructeur de la classe Carte, defini une carte avec un prix en Pieces et en Ressources
     * @param nomCarte nom de la carte
     * @param points nb de points que permet de gagner la carte
     * @param prixCartePieces prix de la carte en pieces
     * @param prixCarteRessources prix en ressource de la carte
     */
    public Carte(String nomCarte, int points, int prixCartePieces, ArrayList<Ressource> prixCarteRessources){
        this.nomCarte = nomCarte;
        if (points == 0){ this.pointsCarte = 1; }
        else{ this.pointsCarte = points; }
        this.prixCartePieces = prixCartePieces;
        this.prixCarteRessources = prixCarteRessources;
    }

    /**
     * Méthode qui permet de connaitre le type de cout de la carte a travers l'entier retourné
     * @return 0 pas de prix, 1 prix en pièces et ressources, 2 prix en pièces uniquement, 3 prix en ressources uniquement
     */
    public int estPayante(){
        if(this.prixCartePieces>0 & this.prixCarteRessources != null){ return 1; }
        else if(this.prixCartePieces > 0 & this.prixCarteRessources == null){ return 2; }
        else if(this.prixCartePieces == 0 & this.prixCarteRessources != null){ return 3;}
        else { return 0; }
    }
    public Carte(String nomCarte, int points, int prixCartePieces,int pointsMilitaires ,ArrayList<Ressource> prixCarteRessources){
        this.nomCarte = nomCarte;
        if (points == 0){ this.pointsCarte = 1; }
        else{ this.pointsCarte = points; }
        this.prixCartePieces = prixCartePieces;
        this.pointsMilitaireCarte  = pointsMilitaires;
        this.prixCarteRessources = prixCarteRessources;
    }

    /*---------- Geteurs ----------*/
    public String getNomCarte() { return nomCarte; }
    public int getPointsCarte() { return pointsCarte; }
    public int getPointsMiliaraireCarte() { return pointsMilitaireCarte;}
    public int getPrixCartePieces() { return prixCartePieces; }
    public ArrayList<Ressource> getPrixCarteRessources(){ return prixCarteRessources; }


    public String toString() {
        return "Carte";
    }

    public boolean equals(Object o) {
        if (o instanceof Carte) {
            Carte c = (Carte) o;

            return true;
        }
        return false;
    }
}
