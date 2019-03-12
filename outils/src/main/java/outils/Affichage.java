package outils;

import java.util.ArrayList;

/**
 * Classe qui gère l'affichage des message console
 */
public class Affichage {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";

    private String couleur;
    private String prefix;

    private ArrayList<String> couleursDispo= new ArrayList<String>();

    /**
     * Constructeur de la classe Affichage avec prefix
     * @param couleur couleur d'affichage, choisir entre RED/GREEN/YELLOW/BLUE/PURPLE/CYAN
     * @param prefix prefix a ajouter avant chaque msg
     */
    public Affichage(String couleur, String prefix){

        couleursDispo.add("BLUE");
        couleursDispo.add("RED");
        couleursDispo.add("GREEN");
        couleursDispo.add("YELLOW");
        couleursDispo.add("PURPLE");
        couleursDispo.add("CYAN");

        setCouleur(couleur);
        this.prefix = prefix;
    }

    /**
     * Constructeur de la classe Affichage sans prefix
     * @param couleur couleur d'affichage, choisir entre RED/GREEN/YELLOW/BLUE/PURPLE/CYAN
     */
    public Affichage(String couleur){

        couleursDispo.add("BLUE");
        couleursDispo.add("RED");
        couleursDispo.add("GREEN");
        couleursDispo.add("YELLOW");
        couleursDispo.add("PURPLE");
        couleursDispo.add("CYAN");

        setCouleur(couleur);
    }

    /**
     * Constructeur de la classe Affichage par default
     */
    public Affichage(){
        couleursDispo.add("BLUE");
        couleursDispo.add("RED");
        couleursDispo.add("GREEN");
        couleursDispo.add("YELLOW");
        couleursDispo.add("PURPLE");
        couleursDispo.add("CYAN");
    }

    /**
     * Méthode qui affiche avec la méthode System.out.prinln
     * @param txt String a afficher
     */
    public void afficher(String txt){
        System.out.println(couleur + prefix + txt + ANSI_RESET);
    }

    /**
     * Méthode privé qui set une couleur jusqu"a qu'elle soit changé encore une fois
     * @param couleur String couleur choisie
     */
    public void setCouleur(String couleur){
        switch (couleur){
            case "RED":
                this.couleur = ANSI_RED;
                break;
            case "GREEN":
                this.couleur = ANSI_GREEN;
                break;
            case "YELLOW":
                this.couleur = ANSI_YELLOW;
                break;
            case "BLUE":
                this.couleur = ANSI_BLUE;
                break;
            case "PURPLE":
                this.couleur = ANSI_PURPLE;
                break;
            case "CYAN":
                this.couleur = ANSI_CYAN;
                break;
            default:
                this.couleur = ANSI_RESET;
                break;
        }
    }

    /*---------- Geteurs ----------*/

    public ArrayList<String> getCouleursDispo() { return couleursDispo; }
    public String getCouleur() { return couleur; }

    /*---------- Setteurs ----------*/

    public void setPrefix(String prefix) { this.prefix = prefix; }
}