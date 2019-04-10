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


/**
 * Classe qui gère la partie réseau côté client
 */
public class Client {

    private Socket connexion;
    private ArrayList<Client> listeJoueursClient = new ArrayList<Client>();

    Joueur j = new Joueur();

    Affichage aff = new Affichage();
    private String couleurClient;

    String getCouleur() {
        return couleurClient;
    }


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

            connexion = IO.socket(ipServeur);

            /*---------- Listenners ---------- */

            /* Listenner pour set la couleur et le numero du joueur */
            connexion.on("infosJoueur", new Emitter.Listener() {
                @Override
                synchronized public void call(Object... objects) {
                    JSONArray infosClientJSON = (JSONArray)objects[0];
                    try{
                        couleurClient = infosClientJSON.get(0).toString();
                        j.setNum(Integer.parseInt(infosClientJSON.get(1).toString()));
                        j.setNomPlateau(infosClientJSON.get(2).toString());
                        //aff.setCouleur(couleurClient);
                        //aff.setPrefix("JOUEUR " + j.getNum() + "->");
                        //aff.afficher("Couleur attribuee : " + couleurClient);
                        // Faut deja choisir un plateau ...
                        //aff.afficher("Plateau attribue : " + j.getNomPlateau());
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
                    ArrayList<String> typesCartes = new ArrayList<String>();
                    typesCartes.add("pointsCarteCivil");
                    typesCartes.add("pointsCarteMilitaire");
                    typesCartes.add("pointsCarteCommercial");
                    typesCartes.add("pointsCarteScientifique");
                    int choixTypeCarteAlea = (int)(Math.random() * 4);


                    try{
                        JSONArray deckCourantJSONArray = (JSONArray)objects[0];
                        ArrayList<Carte> deckCourantClient = new ArrayList<Carte>();

                        for(int i=0;i<deckCourantJSONArray.length();i++){
                            JSONObject carteJSON = new JSONObject(deckCourantJSONArray.get(i).toString());
                            Carte objCarte = new Carte(carteJSON.getString("nomCarte"), typesCartes.get(choixTypeCarteAlea), carteJSON.getInt("pointsCarte"));
                            deckCourantClient.add(objCarte);
                        }

                        j.setMain(deckCourantClient);

                        /*aff.afficher("Le joueur a recu les cartes : ");// + cartesClientCourrantes);
                        for(Carte c : cartesClientCourrantes) {
                            c.getNomCarte();
                        }*/
                        Carte carteJoue = j.choisirCarte();

                        /*aff.afficher("Le joueur a joue la carte "
                                        + carteJoue.getNomCarte()
                                        + " qui vaut " + carteJoue.getPointsCarte() + " points");*/

                        /*aff.afficher("Le joueur a " + getPieces() + " pieces");*/


                        connexion.emit("renvoieCarte", new JSONObject(carteJoue));

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
}
