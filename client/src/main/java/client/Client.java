package client;

import commun.Coup;
import commun.Identification;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Client {

    //temporaire :

    Socket connexion;


    // Objet de synchro
    final Object attenteDéconnexion = new Object();

    public Client(String urlServeur) {

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

            connexion.on("lancerPartie", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("QUE LE JEU COMMENCE !!!");
                }
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

        Client client = new Client("http://127.0.0.1:557");
        client.seConnecter();



        System.out.println("fin du main pour le client");

    }

}