package serveur;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import commun.Carte;
import org.json.JSONArray;
import org.json.JSONObject;


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
    private ArrayList<ArrayList<Plateau>> plateauxDistribués = new ArrayList<ArrayList<Plateau>>();



    /**
     * Constructeur de la classe serveur
     * @param config objet de configuration du serveur
     */
    public Serveur(Configuration config) {

        // creation du serveur
        serveur = new SocketIOServer(config);

        // Objet de synchronisation
        //System.out.println("préparation du listener");

        //instanciation des éléments de jeu
        this.initialisationElementsJeu();

        // on accepte une connexion
        serveur.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient socketIOClient) {
                System.out.println("\nUne connexion effectuee");
                int id = incrementerNbJoueurs(socketIOClient);
	        	donnerNbJoueurs(socketIOClient, id);
                System.out.println("connexion du client numero " + id);
	        	if(id == 4) { // A changer. 4 en version normale
	        		lancerPartie(); 
	        	}
            }
        });

        // Event Listener pour lorque un client envoie un event de renvoi de cartes
        serveur.addEventListener("renvoieCartes", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
                System.out.println("cartesRenvoyées : ");

                // TODO: Encore a remplacer par une fonction / eviter dupliqués
                // Recuperation des cartes non utilisés par le client
                JSONArray cartesRecues = new JSONArray(s);
                decksCirculants.get(0).clear();
                for(int i=0;i<6;i++){
                    String nomCarte = cartesRecues.getJSONObject(i).getString("nomCarte");
                    int nbPointsCarte = cartesRecues.getJSONObject(i).getInt("pointsCarte");
                    Carte carte = new Carte(nomCarte, nbPointsCarte);
                    decksCirculants.get(0).add(carte);
                    System.out.println("Carte #" + i + " : " + decksCirculants.get(0).get(i).getNomCarte()
                            + ";" + decksCirculants.get(0).get(i).getPointsCarte());
                }

            }
        });
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


    public void démarrer() {
        serveur.start();
    }

    public  int incrementerNbJoueurs(SocketIOClient socketIOClient) {
        nbJoueurs++;
        listeClients.add(socketIOClient);
        return nbJoueurs;
    }

    public int getNbJoueur() {
        return nbJoueurs;
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
        	System.out.println("Client connecté");

        	//k k

            //Envoyer du JSON (cartes)
            //JSONObject carteJSON = new JSONObject(new Carte("TestCarte",9000));
            JSONArray cartesCourantesJSON = new JSONArray();

            // TODO : Faudrait creer un envoyer tour a tour a chaque client afin de faire circuler le paquet
            for(Carte uneCarte : decksCirculants.get(0)){
                JSONObject carte = new JSONObject(uneCarte);
                cartesCourantesJSON.put(carte);
            }
            //System.out.println(decksCirculants.toString());



            client.sendEvent("lancerPartie");

            //System.out.println("CarteTest : " + carteJSON.toString());
            System.out.println("Deck : " + cartesCourantesJSON.toString());
        	//client.sendEvent("envoyerCarte", carteJSON.toString());
        	client.sendEvent("envoyerCarte", cartesCourantesJSON.toString());
        }
    }

    /**
     * Méthode qui instancie TOUS éléments de jeu initiaux
     */
    private void initialisationElementsJeu(){
        initialisationDecksCirculants();
    }

    /**
     * Méthode qui instancie les cecks circulants
     */
    private void initialisationDecksCirculants(){
        for(int i=0;i<7;i++){
            decksCirculants.add(new ArrayList<Carte>(7));
        }
        for(ArrayList<Carte> deck : decksCirculants){
            for(int i=0;i<7; i++){
                deck.add(new Carte("Carte"+(i+1),(int)(Math.random()*200)));
            }
        }
    }

    /**
     * Méthode qui initialise les plateaux
     */
    private void initialiserPleateaux(){
        for(int i=0;i<7;i++){
            // Generer les merveilles de manière aléatoire NB + POINTS
            //ArrayList<Merveille>


            String nomPlateau = "plateau"+(i+1);
            //Plateau plateau+(i+1) = new Plateau();
        }
    }

}
