package serveur;

import java.awt.*;
import java.util.ArrayList;

import com.corundumstudio.socketio.SocketIOClient;
import serveur.*;
import commun.*;
import outils.*;
import com.corundumstudio.socketio.Configuration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServeurTest {


    @Test
    public void testChoisirCouleur(){
        ArrayList<String> listecouleur = new ArrayList<String>();

        listecouleur.add("RED");
        listecouleur.add("GREEN");
        listecouleur.add("YELLOW");
        listecouleur.add("PURPLE");
        listecouleur.add("CYAN");

        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(557);
        Serveur unserveur = new Serveur(config);
        String couleur = unserveur.choisirCouleur();
        assertTrue(listecouleur.contains(couleur));
    }
    @Test
    public void testCouleurNonBleu(){
        String unecouleur = "BLUE";
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(557);
        Serveur unserveur = new Serveur(config);
        String couleur = unserveur.choisirCouleur();
        assertNotEquals(unecouleur,couleur);
    }
    @Test
    public void testIntialiserPlateaux(){
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(557);
        ArrayList<Plateau> listePlateaux = new ArrayList<Plateau>();
        ArrayList<Plateau> listePlateauxDistribuables = new ArrayList<Plateau>();
        ArrayList<Merveille> listeMerveilles = new ArrayList<Merveille>();
        int nbMerveilleRand = (int)(Math.random()*4);
        for (int i=0;i<7;i++) {
            for (int j = 0; j < nbMerveilleRand; j++) {
                listeMerveilles.add(new Merveille((int) (Math.random() * 20)));
            }

            String nomPlateau = "plateau" + (i + 1);
            Plateau plt = new Plateau(listeMerveilles, nomPlateau, new Ressource("Pierre", 5));
            listePlateaux.add(plt);
            Serveur unserveur = new Serveur(config);
            listePlateauxDistribuables = unserveur.getPlateauxDistribuables();
        }
        for (int i=0;i<7;i++)
        {
            assertEquals(listePlateaux.get(i).getNomPlateau(),listePlateauxDistribuables.get(i).getNomPlateau());
        }
    }
    /*
    @Test
    public void testRetrouverParticipant()
    {
        SocketIOClient s = new SocketIOClient();
        }
    }*/
}
