package commun;

import java.util.ArrayList;

public class Joueur {
	private String name;
	private int points;
	private Plateau plateauJoueur;
	private ArrayList<Ressource> ressourcesJoueur = new ArrayList<Ressource>();
	private ArrayList<Carte> cartesJoueurCourrantes =  new ArrayList<Carte>();
	private ArrayList<Carte> carteJoueurUtilisees = new ArrayList<Carte>();
	
	public Joueur(int num)
	{
		name = "Joueur" + num;
		points = 0;
		initialiserRessourcesJoueur();
	}

    /**
     * Méthode qui initialise la liste des ressources joueur a 0
     */
	private void initialiserRessourcesJoueur(){
	    ressourcesJoueur.add(new Ressource("Bois",0));
        ressourcesJoueur.add(new Ressource("Or",0));
        ressourcesJoueur.add(new Ressource("Pierre",0));
    }

    /**
     * Fonction qui ajoute une Ressource a un Joueur
     * @param uneRessource la ressource a rajouter
     * @return true si tout s'est bien passé / false sinon
     */
	public Boolean obtenirRessource(Ressource uneRessource){
	    Boolean verif = false;

	    for(Ressource uneRessourceJoueur : ressourcesJoueur){
	        if(uneRessourceJoueur.estDeMemeType(uneRessource)){
	            uneRessourceJoueur.incrementerRessource(uneRessource.getNbRessource());
	            verif = true;
            }
        }

	    return verif;
    }

    /**
     * Méthode qui incremente le nb de points d'un joueur
     * @param nbPoints le nombre de points à rajouter
     */
	public void ajouterPoints(int nbPoints) { this.points += nbPoints; }

    /**
     * Méthode qui permet au Joueur de construire une Merveille
     * @param uneCarte carte utilisé pour construire la Merveille
     * @return true si pas de pb / false sinon
     */
    public Boolean construireMerveille(Carte uneCarte)
    {
        Boolean verif = false;
        if(plateauJoueur.construireMerveilleSuivante(uneCarte)){
            System.out.println("Le joueur a construit la Merveille niveau" + plateauJoueur.getNiveauDeMerveilleActuel());
            int pointsRajouter = plateauJoueur.getListeMerveilles().get(plateauJoueur.getNiveauDeMerveilleActuel()-1).getPointsMerveille();
            this.ajouterPoints(pointsRajouter);
            verif = true;
        }
        return verif;
    }

    /**
     * Méthode qui rajoute une carte utilisé a la liste des cartes utilisées et rajoute les points de la carte au joueur
     * @param uneCarte la carte utilisée
     */
    public void ajouterCarteUtilisee(Carte uneCarte){
        ajouterPoints(uneCarte.getPointsCarte());
        carteJoueurUtilisees.add(uneCarte);
    }

    /**
     * Methode qui permet au joueur de choisir une carte de manière aléatoire
     */
    public Carte choisirCarte(){
        int rand = (int)(Math.random()* cartesJoueurCourrantes.size()-1);
        Carte carteChoisie = cartesJoueurCourrantes.get(rand);
        cartesJoueurCourrantes.remove(rand);
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
            ajouterPoints(carte.getPointsCarte());
            ajouterCarteUtilisee(carte);
        }
        else{
            if(!construireMerveille(carte)){
                ajouterPoints(carte.getPointsCarte());
                ajouterCarteUtilisee(carte);
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
        return cartesJoueurCourrantes;
    }

    /**
     * Méthode qui permet de choisir un plateau en début de partie parmi ceux restants
     * @param listePlateau
     * @return la liste des Plateau sans le Plateau choisi
     */
    public ArrayList<Plateau> choisirPlateau(ArrayList<Plateau> listePlateau){
        int rand = (int)(Math.random()*listePlateau.size()-1);
        this.plateauJoueur = listePlateau.get(rand);
        listePlateau.remove(rand);
        return listePlateau;
    }

    /*---------- Seteurs ----------*/
    public void setCartesJoueurCourrantes(ArrayList<Carte> cartesJoueurCourrantes) { this.cartesJoueurCourrantes = cartesJoueurCourrantes; }


    /*---------- Geteurs ----------*/
    public ArrayList<Ressource> getRessourcesJoueur() { return ressourcesJoueur; }
    public int getPoints() { return points; }
    public ArrayList<Carte> getCarteJoueurUtilisees() { return carteJoueurUtilisees; }
    public Plateau getPlateauJoueur() { return plateauJoueur; }

    // TODO: Provisoire a enlever plus tard
    public ArrayList<Carte> getCartesJoueurCourrantes() { return cartesJoueurCourrantes; }
}
