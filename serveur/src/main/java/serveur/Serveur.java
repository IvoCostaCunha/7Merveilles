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
import outils.*;

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

    // A changer par une sous liste de plateaux si les face A/B sont gérés
    private ArrayList<Plateau> plateauxDistribuables = new ArrayList<Plateau>();

    private Affichage aff = new Affichage("BLUE","SERVEUR -> ");

    ArrayList<String> couleursDispo;

    /**
     * Constructeur de la classe serveur
     * @param config objet de configuration du serveur
     */
    public Serveur(Configuration config) {

        // creation du serveur
        serveur = new SocketIOServer(config);

        //instanciation des éléments de jeu
        this.initialisationElementsJeu();

        couleursDispo = aff.getCouleursDispo();
        // BLUE enlevé vu que c'est pour le serveur
        couleursDispo.remove(0);

        // on accepte les connexions
        serveur.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient socketIOClient) {
                aff.afficher("Une connexion effectuee");
                int id = incrementerNbJoueurs(socketIOClient);
	        	donnerNbJoueurs(socketIOClient, id);
                aff.afficher("connexion du client numero " + id);
	        	if(id == 4) { // A changer. 4 en version normale
	        		lancerPartie(); 
	        	}
            }
        });

        /* ---------- Listenners ---------- */
        serveur.addEventListener("renvoieCartes", Carte[].class, new DataListener<Carte[]>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Carte[] cartes, AckRequest ackRequest) throws Exception {
                aff.afficher("cartesRenvoyées : "+cartes);
                for(Carte c : cartes) {
                    aff.afficher("[serveur][" +socketIOClient.getSessionId()+"]> Carte : " + c.getNomCarte()
                            + ";" + c.getPointsCarte());
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

    public synchronized int incrementerNbJoueurs(SocketIOClient socketIOClient) {
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
        int numJoueur = 1;
        for(SocketIOClient client: listeClients) {

        	aff.afficher("Client connecté");

        	ArrayList<Object> infosJoueur = new ArrayList<Object>();
        	infosJoueur.add(choisirCouleur());
        	infosJoueur.add(numJoueur);
            client.sendEvent("infosJoueur",infosJoueur);
            //client.sendEvent("msgDebutPartie");

            //Envoyer du JSON (cartes)
            JSONArray cartesCourantesJSON = new JSONArray();

            // TODO : Faudrait creer un envoyer tour a tour a chaque client afin de faire circuler le paquet
            for(Carte uneCarte : decksCirculants.get(0)){
                JSONObject carte = new JSONObject(uneCarte);
                cartesCourantesJSON.put(carte);
            }

            client.sendEvent("lancerPartie");
        	client.sendEvent("envoyerCarte", cartesCourantesJSON.toString());

        	numJoueur++;

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
                ArrayList<Ressource> ressources = new ArrayList<Ressource>();
                ressources.add(new Ressource("Bois",5));
                ressources.add(new Ressource("Pierre",3));
                deck.add(new Carte("Carte"+(i+1),(int)(Math.random()*200),3,ressources));
            }
        }
    }

    // TODO: TEST UNITAIRE A FAIRE
    /**
     * Méthode qui initialise les plateaux
     */
    private void initialiserPleateaux(){
        for(int i=0;i<7;i++){
            // Generer les merveilles de manière aléatoire NB + POINTS
            ArrayList<Merveille> merveillesPlateau = new ArrayList<Merveille>();

            // Génération d'un nombre de Merveilles aléatoire
            int nbMerveilleRand = (int)(Math.random()*4);
            for(int j=0;j<nbMerveilleRand;j++){
                merveillesPlateau.add(new Merveille((int)(Math.random()*20)));
            }

            String nomPlateau = "plateau"+(i+1);
            Plateau plt = new Plateau(merveillesPlateau,nomPlateau,new Ressource("Pierre",5));

            // A changer par une sous liste de plateaux si les face A/B sont gérés
            plateauxDistribuables.add(plt);
        }
    }

    /**
     * Méthode qui choisit une couleur au hasard parmi celles disponibles
     */
    public String choisirCouleur(){
        int rand = (int)Math.random()*(couleursDispo.size()-1);
        String couleurChoisie = couleursDispo.get(rand);
        couleursDispo.remove(rand);
        return couleurChoisie;
    }

}
