package commun;

/**
 * @author Ivo COSTA CUNHA on 15/02/2019
 * @project 7Merveilles
 */

import java.util.ArrayList;

/**
 * Classe qui représente le plateau de jeu
 */
public class Plateau {

    private String nomPlateau;
    private ArrayList<Merveille> listeMerveilles;
    private Ressource ressourcePlateau;
    private int niveauMerveilleActuel;
    private int niveauMerveilleMax;
    private String facePlateau;


    /**
     * Constructeur de la classe Plateau avec ajoute à la création des Merveilles
     * @param nomPlateau nom du plateau
     * @param ressourcePlateau ressource contenue dans le plateau
     * @param listeMerveilles ArrayList des Merveilles du Plateau
     */
    public Plateau(ArrayList<Merveille> listeMerveilles,String nomPlateau, Ressource ressourcePlateau,String facePlateau){
        this.nomPlateau = nomPlateau;
        this.ressourcePlateau = ressourcePlateau;
        this.listeMerveilles = listeMerveilles;
        this.facePlateau = facePlateau;
        this.niveauMerveilleMax = listeMerveilles.size();
        this.niveauMerveilleActuel = 0;
    }

    /**
     * Constructeur de la classe Plateau SANS ajout à la création des Merveilles
     * @param nomPlateau nom du plateau
     * @param ressourcePlateau ressource contenue dans le plateau
     * @param niveauMerveillesMax niveau max de merveilles ex: pour un plateau a 3 merveilles : initialisé a 1
     */
    public Plateau(String nomPlateau, Ressource ressourcePlateau, int niveauMerveillesMax){
        this.nomPlateau = nomPlateau;
        this.ressourcePlateau = ressourcePlateau;
        this.niveauMerveilleMax = niveauMerveillesMax;
        this.niveauMerveilleActuel = 0;
        listeMerveilles = new ArrayList<Merveille>(niveauMerveillesMax);
    }

    /*---------- Geteurs ----------*/
    public String getNomPlateau() { return nomPlateau; }
    public ArrayList<Merveille> getListeMerveilles() { return listeMerveilles; }
    public Ressource getRessourcePlateau() { return ressourcePlateau; }
    public int getNiveauDeMerveilleActuel() { return niveauMerveilleActuel; }
    public int getNiveauMerveilleMax() { return  niveauMerveilleMax; }

    /**
     * Fonction qui construit la merveille suivante disponible
     * @return true si la merveille c'est bien construite / false si une erreur a eu lieu
     */
    public Boolean construireMerveilleSuivante(Carte carteUtilise){
        Boolean verif = false;
        if(niveauMerveilleActuel <= niveauMerveilleMax){
            for(Merveille uneMerveille : listeMerveilles){
                if(uneMerveille.getNumMerveille() == niveauMerveilleActuel+1){
                    uneMerveille.setMerveilleValable();
                    niveauMerveilleActuel++;
                    verif = true;
                }
            }
        }
        return verif;
    }

    /**
     * Méthode qui permet de rajouter des merveilles après la création d'un objet Plateau
     * @param merveilles les merveilles à ajouter
     */
    public void ajouterMerveille(Merveille... merveilles){
        for(Merveille uneMerveille : merveilles){
            uneMerveille.setNumMerveille(listeMerveilles.size()+1);
            this.listeMerveilles.add(uneMerveille);
            niveauMerveilleMax++;
        }
    }
}
