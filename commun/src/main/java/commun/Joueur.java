package commun;

import java.util.ArrayList;

/**
 * @author Ivo COSTA CUNHA on 01/04/2019
 * @project 7Merveilles
 */

/**
 * Classe qui gère les attribus du joueur
 */
public class Joueur {
    private String nom;
    private int num;
    private int points;
    private int pieces;
    private String nomPlateau;
    private Plateau plateau;
    private ArrayList<Ressource> ressources = new ArrayList<Ressource>();
    private ArrayList<Carte> main =  new ArrayList<Carte>();
    private ArrayList<Carte> cartesJouees = new ArrayList<Carte>();

    public Joueur(){
        points = 0;
        pieces = 3;
        initialiserRessourcesClient();
    }

    /*-------------------- Gestion des Ressources Joueur --------------------*/

    /**
     * Méthode qui initialise la liste des ressources joueur a 0
     */
    private void initialiserRessourcesClient(){
        ressources.add(new Ressource("Bois",0));
        ressources.add(new Ressource("Or",0));
        ressources.add(new Ressource("Pierre",0));
        ressources.add(new Ressource("Brique",0));
        ressources.add(new Ressource("Verre",0));
        ressources.add(new Ressource("Papyrus",0));
        ressources.add(new Ressource("Minerai",0));
    }

    /**
     * Fonction qui ajoute une Ressource a un Client
     * @param uneRessource la ressource a rajouter
     * @return true si tout s'est bien passé / false sinon
     */
    public Boolean obtenirRessource(Ressource uneRessource){
        Boolean verif = false;

        for(Ressource uneRessourceClient: ressources){
            if(uneRessourceClient.estDeMemeType(uneRessource)){
                uneRessourceClient.incrementerRessource(uneRessource.getNbRessource());
                verif = true;
            }
        }

        return verif;
    }

    /*-------------------- Gestion des points --------------------*/

    /**
     * Méthode qui incremente le nb de points d'un joueur
     * @param nbPoints le nombre de points à rajouter
     */
    public void ajouterPoints(int nbPoints) { this.points += nbPoints; }

    /*-------------------- Possibilités de jeu --------------------*/

    /**
     * Méthode qui permet au Client de construire une Merveille
     * @param uneCarte carte utilisé pour construire la Merveille
     * @return true si pas de pb / false sinon
     */
    public Boolean construireMerveille(Carte uneCarte)
    {
        Boolean verif = false;

        if(plateau.construireMerveilleSuivante(uneCarte)){
            // TODO: Recuperer la couleur client ???
            System.out.println("Le joueur " + num
                    +" a construit la Merveille niveau" + plateau.getNiveauDeMerveilleActuel());
            int pointsRajouter = plateau.getListeMerveilles().get(plateau.getNiveauDeMerveilleActuel()-1).getPointsMerveille();
            this.ajouterPoints(pointsRajouter);
            verif = true;
        }
        return verif;
    }


    private int getPointsCarte(Carte c) {
        return c.getTypePointsCarte(c);
   }

    /**
     * Méthode qui rajoute une carte utilisé a la liste des cartes utilisées et rajoute les points de la carte au joueur
     * @param uneCarte la carte utilisée
     */
    public void ajouterCarteJoue(Carte uneCarte){
        ajouterPoints(getPointsCarte(uneCarte));
        cartesJouees.add(uneCarte);
    }

    /**
     * Methode qui permet au joueur de choisir une carte de manière aléatoire
     */
    public Carte choisirCarte(){
        int rand = (int)(Math.random()* main.size()-1);
        Carte carteChoisie = main.get(rand);
        main.remove(rand);
        return carteChoisie;
    }


    /**
     * Méthode qui détermine la facon de jouer la carte du bot 50% de utiliser la carte / 50% de chance de constsuire la
     * Merveille
     */
    public void jouer(){
        int rand = (int)(Math.random()*10);
        Carte carte = choisirCarte();
        if(rand > 5){
            ajouterPoints(getPointsCarte(carte));
            ajouterCarteJoue(carte);
        }
        else{
            if(!construireMerveille(carte)){
                ajouterPoints(getPointsCarte(carte));
                ajouterCarteJoue(carte);
            }
            else{
                construireMerveille(carte);
            }
        }
    }

    /**
     * Fonction qui renvoie la liste des cartes après que le joueur en ai pris une
     * @return la liste des cartes qui seront renvoyés a la suite des joueurs pour le tour
     */
    public ArrayList<Carte> renvoyerCartes(){
        return main;
    }

    /**
     * Méthode qui permet de choisir un plateau en début de partie parmi ceux restants
     * @param listePlateau
     * @return la liste des Plateau sans le Plateau choisi
     */
    public ArrayList<Plateau> choisirPlateau(ArrayList<Plateau> listePlateau){
        int rand = (int)(Math.random()*listePlateau.size()-1);
        this.plateau = listePlateau.get(rand);
        listePlateau.remove(rand);
        return listePlateau;
    }

    /*-------------------- Seteurs --------------------*/
    public void setMain(ArrayList<Carte> uneMain) { main = uneMain; }
    public void setNum(int unNum) { num = unNum; }
    public void setNomPlateau(String nomPlateau) { nomPlateau = this.nomPlateau; }


    /*-------------------- Geteurs --------------------*/
    public ArrayList<Ressource> getRessources() { return ressources; }
    public int getPoints() { return points; }
    public ArrayList<Carte> getCartesJouees() { return cartesJouees; }
    public Plateau getPlateau() { return plateau; }
    public int getPieces() { return pieces; }
    public ArrayList<Carte> getMain() { return main; }
    public int getNum() { return num; }
    public String getNomPlateau() { return nomPlateau; }
}
