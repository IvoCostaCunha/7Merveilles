package client;

import commun.Joueur;
import commun.Plateau;
import commun.Merveille;
import commun.Carte;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import com.google.gson.*;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Client {

    Socket connexion;
    ArrayList<Joueur> listeJoueursClient = new ArrayList<Joueur>();
    ArrayList<Plateau> listePlateauxClient = new ArrayList<Plateau>();
    ArrayList<Carte> listeCarteClient = new ArrayList<Carte>();


    // Objet de synchro
    final Object attenteDéconnexion = new Object();

    public Client(String urlServeur) {
    	// On fait en sorte que des joueurs soient crées
    	
    	
        try {
            connexion = IO.socket(urlServeur);

            System.out.println("Vous avez rejoint la partie");

            connexion.on("connect", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {

                    connexion.emit("rejoindrePartie");
                }
            });

            connexion.on("disconnect", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("Deconnexion");
                    connexion.disconnect();
                    connexion.close();

                    synchronized (attenteDéconnexion) {
                        attenteDéconnexion.notify();
                    }
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
                    String strJson = (String) objects[0];
                    Gson g =  new Gson();
                    Carte c = g.fromJson(strJson, Carte.class);
                    System.out.println("Le nom de la carte : " + c.getNomCarte());
                    System.out.println("Le nombre de points de la carte" + c.getPointsCarte());
                }
            });

            connexion.on("lancerPartie", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("QUE LE JEU COMMENCE !!!");
                   /* for (int i=0;i<4;i++)
                	{
                		listeJoueursClient.add(new Joueur(i));
                		System.out.println("Joueur"+i+" prêt");

                	}*/
                	// Ces joueurs choisissent leurs cartes
                	/*for (Joueur j : listeJoueursClient)
                	{
                		listePlateauxClient = j.choisirPlateau(listePlateauxClient);
                		// On distribue 7 cartes pour chaque joueur
                		for (int i = 0;i<7;i++)
                		{
                			//listeCarteClient.add(j.choisirCarte());
                			System.out.println("Distribution d'une carte");
                		}*/
                	}
                //}
            });



        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void seConnecter() {
        // on se connecte
        connexion.connect();

        //System.out.println("en attente de déconnexion");
        synchronized (attenteDéconnexion) {
            try {
                attenteDéconnexion.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("erreur dans l'attente");
            }
        }
    }

    public static final void main(String []args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Client client = new Client("http://127.0.0.1:131");
        client.seConnecter();



        System.out.println("fin du main pour le client");

    }

}
