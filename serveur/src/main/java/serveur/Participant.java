package serveur;

import com.corundumstudio.socketio.SocketIOClient;
import commun.*;
import java.util.ArrayList;

public class Participant {
    //Mettre en prive et faire getteurs et setteurs
    // Les attibuts peubvent être privé mais pas la classe car elle est à un trop haut niveau hiérarchique

    private String couleur;
    private String plateau;
    private int numeroParticipants;
    private int nbPtsCartes;
    private int nbPlateaux;

    public String getCouleur()
    {
        return this.couleur;
    }
    public void  setCouleur(String couleur)
    {
        this.couleur = couleur;
    }
    public String getPlateau()
    {
        return plateau;
    }
    public void setPlateau(String plateau)
    {
        this.plateau = plateau;
    }
    public int getNb()
    {
        return this.numeroParticipants;
    }
    public void setNb(int nb)
    {
        this.numeroParticipants = nb;
    }
    public  int getNbPts()
    {
        return this.nbPtsCartes;
    }
    public void setNbPts(int nbPts)
    {
        this.nbPtsCartes = nbPts;
    }
    public int getNbPlateaux(){ return this.nbPlateaux;};

    public SocketIOClient client;

    public ArrayList<Carte> cartes;
    public ArrayList<Plateau> plateaux;
}