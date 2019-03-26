package serveur;

import com.corundumstudio.socketio.SocketIOClient;
import commun.Carte;
import java.util.ArrayList;

public class Participant {
    //Mettre en prive et faire getteurs et setteurs

    public String couleur;
    public String plateau;
    public int nb;
    public int nbPts;

    public SocketIOClient client;

    public ArrayList<Carte> cartes;
}