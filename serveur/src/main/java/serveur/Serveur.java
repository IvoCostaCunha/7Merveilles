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

    private int nbJoues = 0;

    /* ---------- Infos clients connectés ---------- */
    private int nbJoueurs = 0;
    private ArrayList<Participant> listeClients = new ArrayList<>();

    private int plateauxDistrib = 0;

    /*---------- Infos sur les cartes ----------*/
    private int positionCirculation = 0;

    /* ---------- Elements de jeu initiaux ---------- */
    private final ArrayList<ArrayList<Carte>> decksCirculants = new ArrayList<ArrayList<Carte>>();
    // A changer par une sous liste de plateaux si les face A/B sont gérés
    private ArrayList<Plateau> plateauxDistribuables = new ArrayList<Plateau>();

    private Affichage aff = new Affichage("YELLOW","SERVEUR -> ");

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

                if(nbJoueurs == 2) { // == nb de joueurs qu'on veut
                    lancerPartie();
                }
            }
        });

        // le joueur renvoie la carte qu'il joue // (ou qu'il défausse) // (ou etape de merveille)
        // on retire la carte jouee de la main du joueur
        // on compte un coup de jouer en plus
        // si le nb de coup joues vaut le nombre de joueur ET qu'il reste 2 cartes par joueurs => on change de tour => jouerTour
        // si le nb de coup joues vaut le nombre de joueur ET qu'il reste 1 carte par joueurs => fin de l'age
        serveur.addEventListener("renvoieCarte", Carte.class, new DataListener<Carte>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Carte carte, AckRequest ackRequest) throws Exception {
                Participant p =  retrouverParticipant(socketIOClient);
                aff.setCouleur(p.getCouleur());
                aff.afficher("Le joueur num" + p.getNb() + " a joue " + carte.getNomCarte().toString());
                //p.nb += carte.getPointsCarte();
                p.setNbPts(p.getNb()+ carte.getPointsCarte());
                aff.setCouleur("YELLOW");
                aff.afficher("le joueur a " + p.getNbPts() + " points");

                if(nbJoues == nbJoueurs && p.cartes.size() == 2) { //Comprend pas la logique
                    jouerTour();
                }else if(nbJoues == nbJoueurs && p.cartes.size() == 1){
                    finDeLAge();
                }

                p.cartes.remove(carte);
                nbJoues++;
                System.out.println("nbJoues = " + nbJoues);
                if(nbJoues == nbJoueurs) {
                    System.out.println("On change de tour ?");
                }
            }
            
         });
    }

    public void finDeLAge() {
        aff.afficher("- L'age est terminé -");
    }


    Participant retrouverParticipant(SocketIOClient s) {
        Participant j = null;

        for(Participant p : listeClients) {
            if (p.client.getRemoteAddress().toString().equals(s.getRemoteAddress().toString())) {
                j = p;
                break;
            }
        }


        return j;
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


        Participant p = new Participant();

        ArrayList<Object> infosJoueur = new ArrayList<Object>();
        p.setCouleur(choisirCouleur());
                infosJoueur.add(p.getCouleur());

        //p.nb = nbJoueurs+1;
        p.setNb(nbJoueurs+1);
        infosJoueur.add(p.getNb());

        p.setPlateau(choisirPlateau());
        infosJoueur.add(p.getPlateau());

        socketIOClient.sendEvent("infosJoueur", infosJoueur);

        donnerNbJoueurs(socketIOClient, nbJoueurs);

        p.client = socketIOClient;

        aff.afficher("connexion du client numero " + (nbJoueurs+1) + "; ID : " + socketIOClient.getSessionId());

        nbJoueurs++;

        listeClients.add(p);
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
    private void lancerPartie() {
        aff.afficher("Le jeu commence, les cartes sont distribuees : ");
        setNbCoupsJoues(0);

        for(int i = 0; i < listeClients.size(); i++ ){
            Participant p = listeClients.get(i);
            p.cartes = decksCirculants.get(i);
            aff.afficher("Liste des cartes distribuees pour le joueur num" + p.getNb());
            for(Carte c : p.cartes) {
                aff.afficher(c.getNomCarte() + " qui vaut " + c.getPointsCarte());
            }
            System.out.println("\n\n");
        }
        jouerTour();
    }


    private synchronized void setNbCoupsJoues(int nb) {
        this.nbJoues = nb;
    }


    private  void jouerTour() {
        nbJoues++;

        aff.setCouleur("YELLOW");
        aff.afficher("- Tour numero " + nbJoues + " -");
        positionCirculation = 0;
        // On reset la circulation des decks quand un tour a été fait
        // faire tourner les mains / decks
        
        if(positionCirculation == nbJoueurs-1){
            positionCirculation = 1;
        }
        else{ positionCirculation++; }
        


        // associer SocketIOClient et la main
        // classe Participant : SocketIOClient, Main, Merveille, ses cartes Jouees...

        // pour chaque participant, on envoie ses cartes

            for(Participant client: listeClients){
                client.client.sendEvent("jouerTour");
                client.client.sendEvent("envoyerCarte", client.cartes);            
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
