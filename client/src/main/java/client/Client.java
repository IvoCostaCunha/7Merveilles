package client;

import commun.*;
import outils.*;

// Imports SocketIO
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

// Imports JSON
import org.json.*;

// Autres imports
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;

// TODO: Faut arreter les clients si le serveur meurt, kill les processus ...


public class Client {

    // PARTIE RESEAU
    private Socket connexion;
    private ArrayList<Joueur> listeJoueursClient = new ArrayList<Joueur>();

    Affichage aff = new Affichage();

    // ATTRIBUTS CLIENT
    private String nom;
    private int numJoueur;
    private String couleurJoueur;
    private int points;
    private int pieces;
    private Plateau plateauJoueur;
    private ArrayList<Ressource> ressourcesJoueur = new ArrayList<Ressource>();
    private ArrayList<Carte> cartesJoueurCourrantes =  new ArrayList<Carte>();
    private ArrayList<Carte> carteJoueurUtilisees = new ArrayList<Carte>();


    // Objet de synchro
    final Object attenteDéconnexion = new Object();

    /* ---------- MAIN ---------- */

    public static final void main(String []args) {
        try { System.setOut(new PrintStream(System.out, true, "UTF-8")); }
        catch (UnsupportedEncodingException e) { e.printStackTrace(); }

        Client client = new Client("http://127.0.0.1:557");
        client.seConnecter();

        System.out.println("fin du main pour le client");
    }

    /**
     * Construit un objet Client
     * @param ipServeur IP de l'adresse a qui connecter l'objet Client
     */
    public Client(String ipServeur) {
        try {

            points = 0;
            pieces = 3;

            connexion = IO.socket(ipServeur);

            /*---------- Listenners ---------- */

            /* Listenner pour set la couleur et le numero du joueur */
            connexion.on("infosJoueur", new Emitter.Listener() {
                @Override
                synchronized public void call(Object... objects) {
                    JSONArray infosJoueurJSON = (JSONArray)objects[0];
                    ArrayList<String> infosJoueur = new ArrayList<String>();
                    try{
                        couleurJoueur = infosJoueurJSON.get(0).toString();
                        numJoueur = Integer.parseInt(infosJoueurJSON.get(1).toString());
                        aff.setCouleur(couleurJoueur);
                        aff = new Affichage(couleurJoueur,"JOUEUR " + numJoueur + " -> ");
                        aff.afficher("Couleur attribuee : " + couleurJoueur);
                    }
                    catch (Exception e){ System.out.println(e.toString()); }

                }
            });


            connexion.on("connect", new Emitter.Listener() {
                @Override
                public void call(Object... objects) { connexion.emit("rejoindrePartie"); }
            });

            connexion.on("disconnect", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    aff.afficher("Deconnexion");
                    connexion.disconnect();
                    connexion.close();
                    synchronized (attenteDéconnexion) { attenteDéconnexion.notify(); }
                }
            });

            /*connexion.on("nbJoueurs", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    aff.afficher("Vous êtes le joueur numero" + objects[0]);
                }
            });*/

            connexion.on("envoyerCarte", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    //System.out.println("carte : " + objects[0]);
                    //String strCarteTest = (String) objects[0];
                    String strDeckCourant = (String) objects[0];
                    try{
                        JSONArray deckCourantJSONArray = new JSONArray(strDeckCourant);
                        ArrayList<Carte> deckCourantJoueur = new ArrayList<Carte>(7);
                        for(int i=0;i<7;i++){
                            JSONObject carteJSON = new JSONObject(deckCourantJSONArray.get(i).toString());
                            Carte objCarte = new Carte(carteJSON.getString("nomCarte"),carteJSON.getInt("pointsCarte"));
                            deckCourantJoueur.add(objCarte);
                        }
                        cartesJoueurCourrantes = deckCourantJoueur;
                        Carte carteJoue = choisirCarte();
                        aff.afficher("Le joueur a joué la carte "
                                        + carteJoue.getNomCarte()
                                        + " qui vaut " + carteJoue.getPointsCarte() + " points");
                        aff.afficher("Le joueur a " + getPieces() + " pieces");

                        JSONArray cartesRenvoyerJSONArray = new JSONArray();

                        // TODO: faut en faire une fonction dans une classe du genre Util.OutilsJSON.java qui permettrait d'eviter le code dupliqué
                        for(Carte uneCarte : cartesJoueurCourrantes){
                            JSONObject carte = new JSONObject(uneCarte);
                            cartesRenvoyerJSONArray.put(carte);
                        }

                        connexion.emit("renvoieCartes",cartesRenvoyerJSONArray);



                    }
                    catch (JSONException e){ System.out.println(e.toString()); }
                }
            });

            connexion.on("msgDebutPartie", new Emitter.Listener() {
                @Override
                public void call(Object... objects) { aff.afficher("Debut de partie ..."); }
            });



        }
        catch (URISyntaxException e) { e.printStackTrace(); }
    }

    public void seConnecter() {
        connexion.connect();

    }

    /**
     * Méthode qui initialise la liste des ressources joueur a 0
     */
    private void initialiserRessourcesJoueur(){
        ressourcesJoueur.add(new Ressource("Bois",0));
        ressourcesJoueur.add(new Ressource("Or",0));
        ressourcesJoueur.add(new Ressource("Pierre",0));
        ressourcesJoueur.add(new Ressource("Brique",0));
        ressourcesJoueur.add(new Ressource("Verre",0));
        ressourcesJoueur.add(new Ressource("Papyrus",0));
        ressourcesJoueur.add(new Ressource("Minerai",0));
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
            aff.afficher("Le joueur a construit la Merveille niveau" + plateauJoueur.getNiveauDeMerveilleActuel());
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
    public int getPieces() { return pieces; }

    // TODO: Provisoire a enlever plus tard
    public ArrayList<Carte> getCartesJoueurCourrantes() { return cartesJoueurCourrantes; }
}
