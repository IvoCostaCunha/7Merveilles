package client;

import commun.*;

// Imports SocketIO
//import com.corundumstudio.socketio.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

// Imports JSON
import org.json.*;

// Import de la lib pour aider avec le JSON
//import jdk.nashorn.internal.parser.JSONParser;
import com.google.gson.*;


// Autres imports
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

// Faut arreter les clients si le serveur meurt, kill les processus ...


public class Client {

    private Socket connexion;
    private ArrayList<Joueur> listeJoueursClient = new ArrayList<Joueur>();
    private ArrayList<Plateau> listePlateauxClient = new ArrayList<Plateau>();
    private ArrayList<Carte> listeCarteClient = new ArrayList<Carte>();


    // Objet de synchro
    final Object attenteDéconnexion = new Object();

    /**
     * Construit un objet Client
     * @param ipServeur IP de l'adresse a qui connecter l'objet Client
     */
    public Client(String ipServeur) {
    	// On fait en sorte que des joueurs soient crées
        try {
            Joueur joueur = new Joueur(1);

            connexion = IO.socket(ipServeur);
            System.out.println("Vous avez rejoint la partie");

            connexion.on("connect", new Emitter.Listener() {
                @Override
                public void call(Object... objects) { connexion.emit("rejoindrePartie"); }
            });

            connexion.on("disconnect", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("Deconnexion");
                    connexion.disconnect();
                    connexion.close();
                    synchronized (attenteDéconnexion) { attenteDéconnexion.notify(); }
                }
            });

            // on recoit une question
           connexion.on("nbJoueurs", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("Vous êtes le joueur numero" + objects[0]);
                }
           });

            connexion.on("envoyerCarte", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    //System.out.println("carte : " + objects[0]);
                    //String strCarteTest = (String) objects[0];
                    String strDeckCourant = (String) objects[0];
                    try{
                        //JSONObject carteTest = new JSONObject(strCarteTest);
                        JSONArray deckCourantJSONArray = new JSONArray(strDeckCourant);
                        //System.out.println(deckCourantJSONArray.toString());
                        ArrayList<Carte> deckCourantJoueur = new ArrayList<Carte>(7);
                        for(int i=0;i<7;i++){
                            //System.out.println(deckCourantJSONArray.get(i));
                            JSONObject carteJSON = new JSONObject(deckCourantJSONArray.get(i).toString());
                            System.out.println(carteJSON);
                            //System.out.println(carteJSON);
                            Carte objCarte = new Carte(carteJSON.getString("nomCarte"),carteJSON.getInt("pointsCarte"));
                            deckCourantJoueur.add(objCarte);
                        }
                        joueur.setCartesJoueurCourrantes(deckCourantJoueur);
                        Carte carteJoue = joueur.choisirCarte();
                        System.out.println("Le joueur a joué la carte "
                                        + carteJoue.getNomCarte()
                                        + " qui vaut " + carteJoue.getPointsCarte() + " points");
                        System.out.println("Le joueur a " + joueur.getRessourcesJoueur().get(7).toString()+"pieces");
                        //System.out.println(carteTest.toString());
                        //System.out.println(carteTest.getString("nomCarte"));

                        JSONArray cartesRenvoyerJSONArray = new JSONArray();

                        // TODO: faut en faire une fonction dans une classe du genre Util.OutilsJSON.java qui permettrait d'eviter le code dupliqué
                        for(Carte uneCarte : joueur.getCartesJoueurCourrantes()){
                            JSONObject carte = new JSONObject(uneCarte);
                            cartesRenvoyerJSONArray.put(carte);
                        }

                        connexion.emit("renvoieCartes",cartesRenvoyerJSONArray);



                    }
                    catch (JSONException e){ System.out.println(e.toString()); }
                    //Gson g =  new Gson();
                    //Carte c = g.fromJson(strJson, Carte.class);
                    //System.out.println("Le nom de la carte : " + c.getNomCarte());
                    //System.out.println("Le nombre de points de la carte : " + c.getPointsCarte());
                }
            });



            /*connexion.on("envoyerCartes", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    String strJson = (String) objects[0];
                    Gson g =  new Gson();
                    List<Carte> listeC = (List<Carte>) g.fromJson(strJson, Carte.class);
                    System.out.println("Le nom de la carte : " + listeC.get(0));
                    System.out.println("Le nombre de points de la carte : " + listeC.get(0).getPointsCarte());
                }
            });*/

            connexion.on("msgDebutPartie", new Emitter.Listener() {
                @Override
                public void call(Object... objects) { System.out.println("QUE LE JEU COMMENCE !!!"); }
            });



        }
        catch (URISyntaxException e) { e.printStackTrace(); }


    }

    public void seConnecter() {
        // on se connecte
        connexion.connect();

    }

    public static final void main(String []args) {
        try { System.setOut(new PrintStream(System.out, true, "UTF-8")); }
        catch (UnsupportedEncodingException e) { e.printStackTrace(); }

        Client client = new Client("http://127.0.0.1:557");
        client.seConnecter();

        System.out.println("fin du main pour le client");
    }

}
