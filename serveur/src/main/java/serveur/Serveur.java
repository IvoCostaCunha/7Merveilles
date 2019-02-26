package serveur;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import commun.*;

/**
 * attend une connexion, on envoie une question puis on attend une réponse, jusqu'à la découverte de la bonne réponse
 * le client s'identifie (som, niveau)
 */
public class Serveur {

    private SocketIOServer serveur;
    private final Object attenteConnexion = new Object();

    /* ---------- Infos clients connectés ---------- */
    private int nbJoueurs = 0;
    private ArrayList<SocketIOClient> listeClients = new ArrayList<SocketIOClient>();

    /* ---------- Elements de jeu initiaux ---------- */
    private ArrayList<ArrayList<Carte>> decksCirculants = new ArrayList<ArrayList<Carte>>();
    private ArrayList<Carte> deckCirculant1 = new ArrayList<Carte>(7);
    private ArrayList<Carte> deckCirculant2 = new ArrayList<Carte>(7);
    private ArrayList<Carte> deckCirculant3 = new ArrayList<Carte>(7);
    private ArrayList<Carte> deckCirculant4 = new ArrayList<Carte>(7);




    public void incrementerNbJoueurs() {
    	nbJoueurs++;
    }

    public int getNbJoueur() {
    	return nbJoueurs;
    }

    /**
     * Constructeur de la classe serveur
     * @param config objet de configuration du serveur
     */
    public Serveur(Configuration config) {
        // Création du serveur
        serveur = new SocketIOServer(config);

        // Objet de synchronisation
        System.out.println("préparation du listener");

        // on accepte une connexion
        serveur.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient socketIOClient) {
            	listeClients.add(socketIOClient);
                System.out.println("\nUne connexion effectuee");
                incrementerNbJoueurs();
	        	donnerNbJoueurs(socketIOClient, nbJoueurs);
                System.out.println("connexion du client numero " + nbJoueurs);
	        	if(nbJoueurs == 4) { lancerPartie(); }
            }
        });


        /*serveur.addEventListener("rejoindrePartie", Object.class, new DataListener<Object>() {
	    @Override
	    public void onData(SocketIOClient socketIOClient, Object objVide, AckRequest ackRequest) throws Exception {
	        System.out.println("connexion du client numero " + nbJoueurs);
	        }
        });*/



    }

    /* ----------- méthode main ----------- */

    public static final void main(String []args) {
        try { System.setOut(new PrintStream(System.out, true, "UTF-8")); }
        catch (UnsupportedEncodingException e) { e.printStackTrace(); }

        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(557);


        Serveur serveur = new Serveur(config);
        serveur.démarrer();


        System.out.println("fin du main");

    }



    private void démarrer() {

        serveur.start();

        System.out.println("en attente de connexion");
        synchronized (attenteConnexion) {
            try { attenteConnexion.wait(); }
            catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("erreur dans l'attente");
            }
        }

        System.out.println("Une connexion est arrivée, on arrête");
        serveur.stop();

    }


    /**
     * Méthode qui envoie au client le nb de joueurs
     * @param socketIOClient Objet socketIOClient a qui envoyer
     * @param nbJoueurs le nb de joueurs
     */
    private void donnerNbJoueurs(SocketIOClient socketIOClient, int nbJoueurs) {
        socketIOClient.sendEvent("nbJoueurs", nbJoueurs);
    }

    /**
     * Méthode qui lance la partie
     */
    private void lancerPartie() {
        for(SocketIOClient client: listeClients) {
        	client.sendEvent("msgDebutPartie");
        	//Envoyer du JSON (cartes)
        }
    }

    /**
     * Méthode qui instancie TOUS éléments de jeu initiaux
     */
    private void initialisationElemJeu(){
        initialisationDecksCirculants();
    }

    /**
     * Méthode qui instancie les cecks circulants
     */
    private void initialisationDecksCirculants(){
        for(ArrayList<Carte> deck : decksCirculants){
            for(int i=0;i<7; i++){
                deck.add(new Carte("Carte"+(i+1),(int)Math.random()*20));
            }
        }
    }

    /**
     * Méthode qui initialise les plateaux
     */
    private void initialiserPleateaux(){
        for(int i=0;i<7;i++){
            // Generer les merveilles de manière aléatoire NB + POINTS
            ArrayList<Merveille>

            String nomPlateau = "plateau"+(i+1);
            Plateau plateau+(i+1) = new Plateau()
        }
    }


}
