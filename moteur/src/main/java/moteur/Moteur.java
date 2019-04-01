package moteur;

/**
 * @author Ivo COSTA CUNHA on 01/04/2019
 * @project 7Merveilles
 */

import commun.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Classe qui s'occupe de tous les éléments nécéssaires au jeu
 */
public class Moteur {

    private final ArrayList<ArrayList<Carte>> mains = new ArrayList<ArrayList<Carte>>();
    // TODO: A changer par une sous liste de plateaux si les face A/B sont gérés
    private ArrayList<Plateau> plateaux = new ArrayList<Plateau>();

    /**
     * Constructeur par default de la classe Moteur
     */
    public Moteur(){

        initialisationElementsJeu();
    }

    /**
     * Méthode qui génere les éléments de jeu au au début de la partie
     */
    private void initialisationElementsJeu(){
        initialisationMains();
        initialiserPlateaux();
    }

    /**
     * Méthode qui instancie les cecks circulants
     */
    private void initialisationMains(){
        for(int i=0;i<7;i++){
            mains.add(new ArrayList<Carte>(7));
        }
        for(ArrayList<Carte> main : mains){
            for(int i=0;i<7; i++){
                ArrayList<Ressource> ressources = new ArrayList<Ressource>();
                ressources.add(new Ressource("Bois",5));
                ressources.add(new Ressource("Pierre",3));
                main.add(new Carte("Carte - "+ (i+1),(int)(Math.random()*20),3,ressources));
            }
        }
    }

    /**
     * Méthode qui initialise les plateaux
     */
    private void initialiserPlateaux(){
        for(int i=0;i<7;i++){
            int nbMerveilles = 1 + (int)(Math.random() * 5); //Combien de merveille pour le plateau ?
            ArrayList<Merveille> listeMerveilles = new ArrayList<>();
            for(int j = nbMerveilles; j<nbMerveilles; j++) {
                Merveille nouvMerveille = new Merveille(j);
                listeMerveilles.add(nouvMerveille);
            }
            String nomPlateau = "plateau" + i; //Nom du plateau

            Plateau plt = new Plateau(listeMerveilles, nomPlateau, new Ressource("Pierre",5));

            // A changer par une sous liste de plateaux si les face A/B sont gérés
            plateaux.add(plt);
        }
    }

    /*-------------------- Seteurs --------------------*/

    /*-------------------- Geteurs --------------------*/
    public ArrayList<Plateau> getPlateaux() { return plateaux; }
    public ArrayList<ArrayList<Carte>> getMains() { return mains; }
}
