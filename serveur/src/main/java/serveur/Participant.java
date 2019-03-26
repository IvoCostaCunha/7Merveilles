package serveur;

import com.corundumstudio.socketio.SocketIOClient;
import commun.Carte;
import java.util.ArrayList;

public class Participant {
    //Mettre en prive et faire getteurs et setteurs
    // Les attibuts peubvent être privé mais pas la classe car elle est à un trop haut niveau hiérarchique

    private String couleur;
    private String plateau;
    private int nb;
    private int nbPts;

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
        return this.nb;
    }
    public void setNb(int nb)
    {
        this.nb = nb;
    }
    public  int getNbPts()
    {
        return this.nbPts;
    }
    public void setNbPts(int nbPts)
    {
        this.nbPts = nbPts;
    }

    public SocketIOClient client;

    public ArrayList<Carte> cartes;
}