package serveur;

import java.util.ArrayList;

import com.corundumstudio.socketio.SocketIOClient;
import serveur.*;
import com.corundumstudio.socketio.Configuration;

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
    public void testAjoutMain() {
    	Participant p;
    	assertTrue(Serveur.lancerTour());
    }

    @Test
    public void retrouverParticipant(SocketIOClient s) {
    for(Participant p : listeClients) {
            boolean findParticipant;
            if (p.client.getRemoteAddress().toString().equals(s.getRemoteAddress().toString())) {
                findParticipant = true;
                break;
            }
            asserTrue(findParticipant);
        }

}
