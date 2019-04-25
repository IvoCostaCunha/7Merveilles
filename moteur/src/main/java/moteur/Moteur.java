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
    Merveille MerveilleUn;
    Merveille MerveilleDeux;
    Merveille MerveilleTrois;

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
    public void initialisationMains(){
        ArrayList<String> typesCartes = new ArrayList<String>();
        typesCartes.add("pointsCarteCivil");
        typesCartes.add("pointsCarteMilitaire");
        typesCartes.add("pointsCarteCommercial");
        typesCartes.add("pointsCarteScientifique");
        for(int i=0;i<7;i++){
            mains.add(new ArrayList<Carte>(7));
        }
        for(ArrayList<Carte> main : mains){
            for(int i=0;i<7; i++){
                ArrayList<Ressource> ressources = new ArrayList<Ressource>();
                ressources.add(new Ressource("Bois",5));
                ressources.add(new Ressource("Pierre",3));
                int choixTypeCarteAlea = (int)(Math.random() * 4);
                int valeurCarte = (int)(Math.random()*20);
                //main.add(new Carte("Carte - "+ (i+1),(int)(Math.random()*20),3,ressources));
                String typeCarte = typesCartes.get(choixTypeCarteAlea);
                main.add(new Carte("Carte - "+ (i+1), typeCarte, valeurCarte));
            }
        }
    }

    /**
     * Méthode qui initialise les plateaux
     */
    private void initialiserPlateaux(){
        ArrayList<String> NomsPlateaux = new ArrayList<String>();
        NomsPlateaux.add("LeColosseDeRhodes");
        NomsPlateaux.add("TempleEphesos");
        NomsPlateaux.add("PyramideDeGizeh");
        NomsPlateaux.add("JardinsSuspendusDeBabylone");
        NomsPlateaux.add("StatueDeZeusAOlympie");
        NomsPlateaux.add("PhareDAlexandrie");
        NomsPlateaux.add("MausoléDHalicarnasse");

        for(String unNomPlateau : NomsPlateaux)
        {
            // TODO: Il faut gérer les cartes scientifiques dans l'avenir ainsi que les effes de certaines merveilles
            int nbMerveilles = 3;
            ArrayList<Merveille> listeMerveilles = new ArrayList<>();

            switch(unNomPlateau)
            {
                case "LeColosseDeRhodes":
                MerveilleUn = new Merveille(1,3,0,"");
                MerveilleDeux = new Merveille(2,0,2,"");
                MerveilleTrois = new Merveille(3,7,0,"");

                listeMerveilles.add(MerveilleUn);
                listeMerveilles.add(MerveilleDeux);
                listeMerveilles.add(MerveilleTrois);

                Plateau pltColosseDeRhodes = new Plateau(listeMerveilles, unNomPlateau, new Ressource("Minerai",1),"A");
                plateaux.add(pltColosseDeRhodes);
                listeMerveilles.clear();
                break;

                case "TempleEphesos":
                MerveilleUn = new Merveille(1,3,0,"");
                MerveilleDeux = new Merveille(2,0,2,"");
                MerveilleTrois = new Merveille(3,7,0,"");

                listeMerveilles.add(MerveilleUn);
                listeMerveilles.add(MerveilleDeux);
                listeMerveilles.add(MerveilleTrois);

                Plateau pltTempleEphesos = new Plateau(listeMerveilles, unNomPlateau, new Ressource("Papier",1),"A");
                plateaux.add(pltTempleEphesos);
                listeMerveilles.clear();
                break;

                case "PyramideDeGizeh":

                MerveilleUn = new Merveille(1,3,0,"");
                MerveilleDeux = new Merveille(2,5,0,"");
                MerveilleTrois = new Merveille(3,7,0,"");

                listeMerveilles.add(MerveilleUn);
                listeMerveilles.add(MerveilleDeux);
                listeMerveilles.add(MerveilleTrois);

                Plateau pltPyramideDeGizeh = new Plateau(listeMerveilles, unNomPlateau, new Ressource("Pierre",1),"A");
                plateaux.add(pltPyramideDeGizeh);
                listeMerveilles.clear();
                break;

                case "JardinsSuspendusDeBabylone" :

                MerveilleUn = new Merveille(1,3,0,"");
                MerveilleDeux = new Merveille(2,0,2,"Récupérer un instrument de science");
                MerveilleTrois = new Merveille(3,7,0,"");

                listeMerveilles.add(MerveilleUn);
                listeMerveilles.add(MerveilleDeux);
                listeMerveilles.add(MerveilleTrois);

                Plateau pltJardinsDeBabylone = new Plateau(listeMerveilles, unNomPlateau, new Ressource("Brique",1),"A");
                plateaux.add(pltJardinsDeBabylone);
                listeMerveilles.clear();
                break;

                case "StatueDeZeusAOlympie" :

                MerveilleUn = new Merveille(1,3,0,"");
                MerveilleDeux = new Merveille(2,0,2,"");
                MerveilleTrois = new Merveille(3,7,0,"");

                listeMerveilles.add(MerveilleUn);
                listeMerveilles.add(MerveilleDeux);
                listeMerveilles.add(MerveilleTrois);

                Plateau pltStatueDeZeusAOlympie = new Plateau(listeMerveilles, unNomPlateau, new Ressource("Bois",1),"A");
                plateaux.add(pltStatueDeZeusAOlympie);
                listeMerveilles.clear();
                break;

                case "PhareDAlexandrie" :

                MerveilleUn = new Merveille(1,3,0,"");
                MerveilleDeux = new Merveille(0,0,0,"On peut construire une merveille gratuitement à chaque age");
                MerveilleTrois = new Merveille(3,7,0,"");

                listeMerveilles.add(MerveilleUn);
                listeMerveilles.add(MerveilleDeux);
                listeMerveilles.add(MerveilleTrois);

                Plateau pltPhareDAlexandrie = new Plateau(listeMerveilles, unNomPlateau, new Ressource("Fiole",1),"A");
                plateaux.add(pltPhareDAlexandrie);
                listeMerveilles.clear();
                break;

                case "MausoléDHalicarnasse" :

                MerveilleUn = new Merveille(1,3,0,"");
                MerveilleDeux = new Merveille(2,0,0,"Choisissez une carte dans la défausse, utilisé la sans prendre en compte la ressource");
                MerveilleTrois = new Merveille(3,7,0,"");

                listeMerveilles.add(MerveilleUn);
                listeMerveilles.add(MerveilleDeux);
                listeMerveilles.add(MerveilleTrois);

                Plateau pltMausoléDHalicarnasse = new Plateau(listeMerveilles, unNomPlateau, new Ressource("Tissu",1),"A");
                plateaux.add(pltMausoléDHalicarnasse);
                listeMerveilles.clear();
                break;

            }

            /*
            int nbMerveillesColosseDeRhodes = 3;  //Combien de merveille pour le plateau ?
            int nbMerveillesTempleEphesos = 3;
            int nbMerveillesPyramideDeGizeh = 3;
            int nbMerveillesJardinsDeBabylone = 3;
            int nbMerveillesStatueDeZeusAOlympie = 3;
            int nbMerveillesPhareDAlexandrie = 3;
            int nbMerveillesMausoléDHalicarnasse = 3; */


            //String nomPlateau = "plateau" + i; //Nom du plateau
            /*
            Plateau pltColosseDeRhodes = new Plateau(listeMerveilles, "LeColosseDeRhodes", new Ressource("Minerai",1),"A");
            Plateau pltTempleEphesos = new Plateau(listeMerveilles, "TempleEphesos", new Ressource("Papier",1),"A");
            Plateau pltPyramideDeGizeh = new Plateau(listeMerveilles, "PyramideDeGizeh", new Ressource("Pierre",1),"A");
            Plateau pltJardinsDeBabylone = new Plateau(listeMerveilles, "JardinSuspenduesDeBabylone", new Ressource("Brique",1),"A");
            Plateau pltStatueDeZeusAOlympie = new Plateau(listeMerveilles, "StatueDeZeusAOlympie", new Ressource("Bois",1),"A");
            Plateau pltPhareDAlexandrie = new Plateau(listeMerveilles,"PhareDAlexandrie",new Ressource("Fiole",1),"A");
            Plateau pltMausoléDHalicarnasse = new Plateau(listeMerveilles,"MausoléDHalicarnasse",new Ressource("Tissu",1),"A");

            // A changer par une sous liste de plateaux si les face A/B sont gérés
            plateaux.add(pltColosseDeRhodes);
            plateaux.add(pltTempleEphesos);
            plateaux.add(pltPyramideDeGizeh);
            plateaux.add(pltJardinsDeBabylone);
            plateaux.add(pltStatueDeZeusAOlympie);
            plateaux.add(pltPhareDAlexandrie);
            plateaux.add(pltMausoléDHalicarnasse);*/
        }
    }

    /*-------------------- Seteurs --------------------*/

    /*-------------------- Geteurs --------------------*/
    public ArrayList<Plateau> getPlateaux() { return plateaux; }
    public ArrayList<ArrayList<Carte>> getMains() { //initialisationMains();
 		return mains; }
    public void newMain() { initialisationMains();}
}
