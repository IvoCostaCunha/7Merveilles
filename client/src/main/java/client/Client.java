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
    private ArrayList<Client> listeJoueursClient = new ArrayList<Client>();

    Affichage aff = new Affichage();

    // ATTRIBUTS CLIENT
    private String nom;
    private int numClient;
    private String couleurClient;
    private int points;
    private int pieces;
    private Plateau plateauClient;
    private ArrayList<Ressource> ressourcesClient = new ArrayList<Ressource>();
    private ArrayList<Carte> cartesClientCourrantes =  new ArrayList<Carte>();
    private ArrayList<Carte> carteClientUtilisees = new ArrayList<Carte>();


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
            initialiserRessourcesClient();

            connexion = IO.socket(ipServeur);

            /*---------- Listenners ---------- */

            /* Listenner pour set la couleur et le numero du joueur */
            connexion.on("infosJoueur", new Emitter.Listener() {
                @Override
                synchronized public void call(Object... objects) {
                    JSONArray infosClientJSON = (JSONArray)objects[0];
                    try{
                        couleurClient = infosClientJSON.get(0).toString();
                        numClient = Integer.parseInt(infosClientJSON.get(1).toString());
                        aff.setCouleur(couleurClient);
                        aff = new Affichage(couleurClient,"JOUEUR " + numClient + " -> ");
                        aff.afficher("Couleur attribuee : " + couleurClient);
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
                    //String strDeckCourant = (String) objects[0];
                    try{
                        JSONArray deckCourantJSONArray = (JSONArray)objects[0];
                        ArrayList<Carte> deckCourantClient = new ArrayList<Carte>();

                        for(int i=0;i<deckCourantJSONArray.length();i++){
                            JSONObject carteJSON = new JSONObject(deckCourantJSONArray.get(i).toString());
                            Carte objCarte = new Carte(carteJSON.getString("nomCarte"),carteJSON.getInt("pointsCarte"));
                            deckCourantClient.add(objCarte);
                        }


                        cartesClientCourrantes = deckCourantClient;

                        aff.afficher("Le joueur a recu les cartes : ");// + cartesClientCourrantes);
                        for(Carte c : cartesClientCourrantes) {
                            c.getNomCarte();
                        }
                        Carte carteJoue = choisirCarte();

                        aff.afficher("Le joueur a joue la carte "
                                        + carteJoue.getNomCarte()
                                        + " qui vaut " + carteJoue.getPointsCarte() + " points");

                        aff.afficher("Le joueur a " + getPieces() + " pieces");

                        JSONArray cartesRenvoyerJSONArray = new JSONArray();

                        // TODO: On peut eviter de passer par le JSONArray peut etre comme ailleurs
                        for(Carte uneCarte : cartesClientCourrantes){
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
    private void initialiserRessourcesClient(){
        ressourcesClient.add(new Ressource("Bois",0));
        ressourcesClient.add(new Ressource("Or",0));
        ressourcesClient.add(new Ressource("Pierre",0));
        ressourcesClient.add(new Ressource("Brique",0));
        ressourcesClient.add(new Ressource("Verre",0));
        ressourcesClient.add(new Ressource("Papyrus",0));
        ressourcesClient.add(new Ressource("Minerai",0));
    }

    /**
     * Fonction qui ajoute une Ressource a un Client
     * @param uneRessource la ressource a rajouter
     * @return true si tout s'est bien passé / false sinon
     */
    public Boolean obtenirRessource(Ressource uneRessource){
        Boolean verif = false;

        for(Ressource uneRessourceClient: ressourcesClient){
            if(uneRessourceClient.estDeMemeType(uneRessource)){
                uneRessourceClient.incrementerRessource(uneRessource.getNbRessource());
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
     * Méthode qui permet au Client de construire une Merveille
     * @param uneCarte carte utilisé pour construire la Merveille
     * @return true si pas de pb / false sinon
     */
    public Boolean construireMerveille(Carte uneCarte)
    {
        Boolean verif = false;
        if(plateauClient.construireMerveilleSuivante(uneCarte)){
            aff.afficher("Le joueur a construit la Merveille niveau" + plateauClient.getNiveauDeMerveilleActuel());
            int pointsRajouter = plateauClient.getListeMerveilles().get(plateauClient.getNiveauDeMerveilleActuel()-1).getPointsMerveille();
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
        carteClientUtilisees.add(uneCarte);
    }

    /**
     * Methode qui permet au joueur de choisir une carte de manière aléatoire
     */
    public Carte choisirCarte(){
        int rand = (int)(Math.random()* cartesClientCourrantes.size()-1);
        Carte carteChoisie = cartesClientCourrantes.get(rand);
        cartesClientCourrantes.remove(rand);
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
        return cartesClientCourrantes;
    }

    /**
     * Méthode qui permet de choisir un plateau en début de partie parmi ceux restants
     * @param listePlateau
     * @return la liste des Plateau sans le Plateau choisi
     */
    public ArrayList<Plateau> choisirPlateau(ArrayList<Plateau> listePlateau){
        int rand = (int)(Math.random()*listePlateau.size()-1);
        this.plateauClient = listePlateau.get(rand);
        listePlateau.remove(rand);
        return listePlateau;
    }

    /*---------- Seteurs ----------*/
    public void setCartesClientCourrantes(ArrayList<Carte> cartesClientCourrantes) { this.cartesClientCourrantes = cartesClientCourrantes; }


    /*---------- Geteurs ----------*/
    public ArrayList<Ressource> getRessourcesClient() { return ressourcesClient; }
    public int getPoints() { return points; }
    public ArrayList<Carte> getCarteClientUtilisees() { return carteClientUtilisees; }
    public Plateau getPlateauClient() { return plateauClient; }
    public int getPieces() { return pieces; }

    // TODO: Provisoire a enlever plus tard
    public ArrayList<Carte> getCartesClientCourrantes() { return cartesClientCourrantes; }
}
