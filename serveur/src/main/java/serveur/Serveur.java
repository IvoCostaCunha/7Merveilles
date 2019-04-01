package serveur;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import commun.Carte;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import commun.*;
import outils.*;
import moteur.*;

/**
 * attend une connexion, on envoie une question puis on attend une réponse, jusqu'à la découverte de la bonne réponse
 * le client s'identifie (som, niveau)
 */
public class Serveur {

    private SocketIOServer serveur;
    private Moteur moteur = new Moteur();

    private final Object attenteConnexion = new Object();

    private int nbJoues = 0;

    /* ---------- Infos clients connectés ---------- */
    private int nbJoueurs = 0;
    private int nbJoueursDistrib = 0; //Nombre de decks distribues
    private ArrayList<Participant> listeClients = new ArrayList<>();

    private int plateauxDistrib = 0;

    /*---------- Infos sur les cartes ----------*/
    private int positionCirculation = 0;

    // Le serveur est en bleu
    private Affichage aff = new Affichage("BLUE","SERVEUR -> ");

    private ArrayList<String> couleursDispo;

    private int nbTours = 0;

    /**
     * Constructeur de la classe serveur
     * @param config objet de configuration du serveur
     */
    public Serveur(Configuration config){

        // creation du serveur
        serveur = new SocketIOServer(config);

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
            public synchronized void onData(SocketIOClient socketIOClient, Carte carte, AckRequest ackRequest) throws Exception {
                Participant p = retrouverParticipant(socketIOClient);
                aff.afficher("Le joueur num" + p.getNb() + " a joue " + carte.getNomCarte().toString());
                p.setNbPts(p.getNbPts()+ carte.getPointsCarte());
                aff.afficher("Le joueur num" + p.getNb() + " a " + p.getNbPts() + " points");

                if(nbJoues == nbJoueurs && p.cartes.size() >= 2) {
                    aff.afficher("-------------------- ! Changement de tour ! --------------------");
                    nbJoues = 0;
                    jouerTour();
                }else if(nbJoues == nbJoueurs && p.cartes.size() == 1){
                    finDeLAge();
                }

                p.cartes.remove(carte);
            }
            
         });
    }

    public void finDeLAge() {
        aff.afficher("-------------------- ! L'age est terminé ! --------------------");
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
            p.cartes = moteur.getMains().get(i);
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
        aff.afficher("- Tour numero " + nbTours + " -");
        positionCirculation = 0;
        // On reset la circulation des decks quand un tour a été fait
        // faire tourner les mains / decks
        
       /* if(positionCirculation == nbJoueurs-1){
            positionCirculation = 1;
        }
        else{ positionCirculation++; }*/
        


        // associer SocketIOClient et la main ????????????????
        // classe Participant : SocketIOClient, Main, Merveille, ses cartes Jouees...

        // pour chaque participant, on envoie ses cartes

            for(Participant client: listeClients){
                //client.client.sendEvent("jouerTour");
                client.client.sendEvent("envoyerCarte", client.cartes);
                aff.afficher("nbJoues=" + nbJoues);
                nbJoues++;            
        }
        //nbJoues = 0;
        nbTours++;
    }


    /**
     * Méthode qui instancie TOUS éléments de jeu initiaux
     */


    private String choisirPlateau() {
        plateauxDistrib++;
        return moteur.getPlateaux().get(plateauxDistrib-1).getNomPlateau();
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
        for(Plateau p : moteur.getPlateaux()) {
            if(p.getNomPlateau() == nom) {
                pTrouve = p;
            }
        }
        return pTrouve;
    }

    /*-------------------- Geteurs --------------------*/


}
