package serveur;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import commun.Carte;
import org.json.JSONObject;


import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * attend une connexion, on envoie une question puis on attend une réponse, jusqu'à la découverte de la bonne réponse
 * le client s'identifie (som, niveau)
 */
public class Serveur {

    SocketIOServer serveur;
    final Object attenteConnexion = new Object();
    int nbJoueurs = 0;
    ArrayList<SocketIOClient> listeClients = new ArrayList<SocketIOClient>();
    ArrayList<Carte> listeCartes = new ArrayList<Carte>();

    Carte carte1 = new Carte("nom de la carte 1", 1);
    Carte carte2 = new Carte("nom de la carte 2", 2);
    Carte carte3 = new Carte("nom de la carte 3", 3);
    Carte carte4 = new Carte("nom de la carte 4", 4);



    public void ajouterJoueur() {
    	nbJoueurs++;
    }

    public int getNbJoueur() {
    	return nbJoueurs;
    }

    public Serveur(Configuration config) {

        listeCartes.add(carte1);
        listeCartes.add(carte2);
        listeCartes.add(carte3);
        listeCartes.add(carte4);

        // creation du serveur
        serveur = new SocketIOServer(config);

        // Objet de synchro

        System.out.println("préparation du listener");

        // on accepte une connexion
        serveur.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient socketIOClient) {
            	listeClients.add(socketIOClient);
                System.out.println("\nUne connexion effectuee");
                nbJoueurs++;
	        	donnerNbJoueurs(socketIOClient, nbJoueurs);
	        	if(nbJoueurs == 4) {
	        		lancerPartie(socketIOClient);
	        	}
            }
        });


        serveur.addEventListener("rejoindrePartie", Object.class, new DataListener<Object>() {
	    @Override
	    public void onData(SocketIOClient socketIOClient, Object objVide, AckRequest ackRequest) throws Exception {

	        System.out.println("connexion du client numero " + nbJoueurs);
	   		}
		});
    }


    public void démarrer() {

        serveur.start();

        System.out.println("en attente de connexion");
        synchronized (attenteConnexion) {
            try {
                attenteConnexion.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("erreur dans l'attente");
            }
        }

        System.out.println("Une connexion est arrivée, on arrête");
        serveur.stop();

    }


    private void donnerNbJoueurs(SocketIOClient socketIOClient, int nbJoueurs) {
        socketIOClient.sendEvent("nbJoueurs", nbJoueurs);
    }

    private void lancerPartie(SocketIOClient socketIOClient) {
        for(SocketIOClient client: listeClients) {
            JSONObject carteJSON = new JSONObject(carte3);
            JSONObject cartesJSON = new JSONObject(listeCartes);
            client.sendEvent("lancerPartie");
            System.out.println(carteJSON);
        	client.sendEvent("envoyerCarte", carteJSON.toString());
        }
    }



    public static final void main(String []args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(131);


        Serveur serveur = new Serveur(config);
        serveur.démarrer();


        System.out.println("fin du main");

    }


}
