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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import commun.*;
import outils.*;

/**
 * attend une connexion, on envoie une question puis on attend une réponse, jusqu'à la découverte de la bonne réponse
 * le client s'identifie (som, niveau)
 */
public class Serveur extends Thread {

    private Lock lock = new ReentrantLock();

    private SocketIOServer serveur;
    private final Object attenteConnexion = new Object();

    /* ---------- Infos clients connectés ---------- */
    private int nbJoueurs = 0;
    private ArrayList<SocketIOClient> listeClients = new ArrayList<SocketIOClient>();

    private int plateauxDistrib = 0;

    /*---------- Infos sur les cartes ----------*/
    private int positionCirculation = 0;

    /* ---------- Elements de jeu initiaux ---------- */
    private final ArrayList<ArrayList<Carte>> decksCirculants = new ArrayList<ArrayList<Carte>>();
    // A changer par une sous liste de plateaux si les face A/B sont gérés
    private ArrayList<Plateau> plateauxDistribuables = new ArrayList<Plateau>();

    private Affichage aff = new Affichage("BLUE","SERVEUR -> ");

    ArrayList<String> couleursDispo;

    /**
     * Constructeur de la classe serveur
     * @param config objet de configuration du serveur
     */
    public Serveur(Configuration config){

        // creation du serveur
        serveur = new SocketIOServer(config);

        //instanciation des éléments de jeu
        this.initialisationElementsJeu();

        couleursDispo = aff.getCouleursDispo();
        // BLUE enlevé vu que c'est pour le serveur
        couleursDispo.remove(0);

        // on accepte les connexions
        serveur.addConnectListener(new ConnectListener() {
            public synchronized void onConnect(SocketIOClient socketIOClient) {
                connexionClient(socketIOClient);
                if(nbJoueurs == 2){ 
                    lancerPartie();
                }
            }
        });

        serveur.addEventListener("renvoieCartes", Carte[].class, new DataListener<Carte[]>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Carte[] cartes, AckRequest ackRequest) throws Exception {
                decksCirculants.get(0).clear();
                //aff.afficher("L'id du client : " + socketIOClient.getSessionId());
                for(Carte c : cartes) {
                    //aff.afficher("Nom de la carte : " + c.getNomCarte() + " - " + c.getPointsCarte() + "pts");
                    decksCirculants.get(positionCirculation).add(c);

                }            
            
                    // Ici le but est de debloquer le thread et de porsuivre lorsque le client retourne une liste
                    // de cartes après en avoir choisi une

                    //lock.unlock();
                    //aff.afficher("Thread delock par le client ID " + socketIOClient.getSessionId());

                }
         });




        /* ---------- Listenners ----------
        serveur.addEventListener("renvoieCartes", Carte[].class, new DataListener<Carte[]>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Carte[] cartes, AckRequest ackRequest) throws Exception {
                aff.afficher("cartesRenvoyées : "+cartes);
                decksCirculants.get(0).clear();
                for(Carte c : cartes) {
                    aff.afficher("[" +socketIOClient.getSessionId()+"]> Carte : " + c.getNomCarte()
                            + ";" + c.getPointsCarte());

                    decksCirculants.get(0).add(c);
                }
                lock.unlock();
            }
        });*/
    }

    public ArrayList<Plateau> getPlateauxDistribuables()
    {
        return plateauxDistribuables;
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

    public int getNbJoueur() {
        return nbJoueurs;
    }

    private synchronized void connexionClient(SocketIOClient socketIOClient){
        ArrayList<Object> infosJoueur = new ArrayList<Object>();
        infosJoueur.add(choisirCouleur());
        infosJoueur.add(nbJoueurs+1);
        infosJoueur.add(choisirPlateau());
        socketIOClient.sendEvent("infosJoueur", infosJoueur);

        donnerNbJoueurs(socketIOClient, nbJoueurs);


        aff.afficher("connexion du client numero " + (nbJoueurs+1) + "; ID : " + socketIOClient.getSessionId());

        nbJoueurs++;

        listeClients.add(socketIOClient);
    }


    /**
     * Méthode qui envoie au client le nb de joueurs
     * @param socketIOClient Objet socketIOClient a qui envoyer
     * @param nbJoueurs le nb de joueurs
     */
    private void donnerNbJoueurs(SocketIOClient socketIOClient, int nbJoueurs){
        socketIOClient.sendEvent("nbJoueurs", nbJoueurs);
    }

    /**
     * Méthode qui lance la partie
     */
    private  void lancerPartie() {
        aff.afficher("Le jeu commence : ");
        for(SocketIOClient client: listeClients){

            client.sendEvent("jouerTour");
            client.sendEvent("envoyerCarte", decksCirculants.get(positionCirculation));

            if(positionCirculation == nbJoueurs-1){
                positionCirculation = 1;
            }
            else{ positionCirculation++; }
        }
    }


    private  void jouerTour() {

            for(SocketIOClient client: listeClients){

                client.sendEvent("jouerTour");
                client.sendEvent("envoyerCarte", decksCirculants.get(positionCirculation));

                // On reset la circulation des decks quand un tour a été fait
                if(positionCirculation == nbJoueurs-1){
                    positionCirculation = 1;
                }
                else{ positionCirculation++; }

                //aff.afficher("Thread lock par le client ID : " + client.getSessionId());
                //aff.afficher(lock.toString());

                // Utilisation de semaphore pour stopper le thread jusqu'a que le serveur est reçu la confirmation que le
                // client a bien joué son rôle durant le tour

                // lock.lock();
            
        }
    }


    /**
     * Méthode qui instancie TOUS éléments de jeu initiaux
     */
    private void initialisationElementsJeu(){
        initialisationDecksCirculants();
        initialiserPlateaux();
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
                deck.add(new Carte("Carte"+(i+1),(int)(Math.random()*20),3,ressources));
            }
        }
    }

    // TODO: TEST UNITAIRE A FAIRE
    /**
     * Méthode qui initialise les plateaux
     */
    private void initialiserPlateaux(){
        for(int i=0;i<7;i++){
            int nbMerveilles = 1 + (int)(Math.random() * 5); //Combien de merveille pour le plateau ?
            ArrayList<Merveille> listeMerveilles = new ArrayList<>();
            for(int j = nbMerveilles; j<nbMerveilles; j++) {
                Merveille nouvMerveille = new Merveille(j);
                listeMerveilles.add(nouvMerveille);
            }
            String nomPlateau = "plateau" + i; //Nom du plateau

            Plateau plt = new Plateau(listeMerveilles, nomPlateau, new Ressource("Pierre",5));

            // A changer par une sous liste de plateaux si les face A/B sont gérés
            plateauxDistribuables.add(plt);
        }
    }

    private String choisirPlateau() {
        plateauxDistrib++;
        return plateauxDistribuables.get(plateauxDistrib-1).getNomPlateau();
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

    public Plateau getPlateauByName(String nom) {
        Plateau pTrouve = null;
        for(Plateau p : plateauxDistribuables) {
            if(p.getNomPlateau() == nom) {
                pTrouve = p;
            }
        }
        return pTrouve;
    }

}
