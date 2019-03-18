package client;

import java.util.ArrayList;
import commun.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ivo COSTA CUNHA on 13/03/2019
 * @project 7Merveilles
 */
public class ClientTest {
    @Test
    public void testInitialisationRessources(){
        Client c = new Client("http://127.0.0.1:557");
        Ressource r1, r2, r3;
        r1 = c.getRessourcesJoueur().get(0);
        r2 = c.getRessourcesJoueur().get(1);
        r3 = c.getRessourcesJoueur().get(2);

        Ressource tr1 = new Ressource("Bois",0);
        Ressource tr2 = new Ressource("Or",0);
        Ressource tr3 = new Ressource("Pierre",0);

        assertTrue((tr1.estDeMemeType(r1)) && (tr2.estDeMemeType(r2)) && (tr3.estDeMemeType(r3)));
    }

    @Test void testObtenirRessource(){
        Client c = new Client("http://127.0.0.1:557");
        c.obtenirRessource(new Ressource("Bois",1));
        assertEquals(1,c.getRessourcesJoueur().get(0).getNbRessource());
    }

    @Test
    public void testAjouterPoints(){
        Client c = new Client("http://127.0.0.1:557");
        c.ajouterPoints(5);
        assertEquals(5,c.getPoints());
    }


    @Test
    public void testRenvoyerCartes(){
        ArrayList<Carte> listeCartes = new ArrayList<Carte>();
        for(int i=0;i<7;i++){
            listeCartes.add(new Carte("Carte n" + i,i));
        }

        Client cl1 = new Client("http://127.0.0.1:557");
        Client cl2 = new Client("http://127.0.0.1:557");

        cl1.setCartesJoueurCourrantes(listeCartes);
        Carte c1 = cl1.choisirCarte();

        cl2.setCartesJoueurCourrantes(cl1.renvoyerCartes());
        Carte c2 = cl2.choisirCarte();

        assertEquals(5,cl2.renvoyerCartes().size());
    }

    @Test
    public void testChoisirCarte(){
        ArrayList<Carte> listeCartes = new ArrayList<Carte>();
        for(int i=0;i<7;i++){
            listeCartes.add(new Carte("Carte n" + i,i));
        }
        Client cl1 = new Client("http://127.0.0.1:557");
        Client cl2 = new Client("http://127.0.0.1:557");

        cl1.setCartesJoueurCourrantes(listeCartes);
        Carte c1 = cl1.choisirCarte();

        cl2.setCartesJoueurCourrantes(cl2.renvoyerCartes());
        Carte c2 = cl1.choisirCarte();

        assertFalse(c1.equals(c2));

    }

    @Test
    public void ajouterCarteUtilise(){
        Client c = new Client("http://127.0.0.1:557");
        Carte carteTest = new Carte("test",420);
        c.ajouterCarteUtilisee(carteTest);
        assertTrue(carteTest.equals(c.getCarteJoueurUtilisees().get(0)));
    }

    @Test
    public void testChoisirPlateau(){
        Client cl1 = new Client("http://127.0.0.1:557");
        Client cl2 = new Client("http://127.0.0.1:557");

        ArrayList<Plateau> listePlateau= new ArrayList<Plateau>();
        for (int i = 0; i < 10; i++) {
            listePlateau.add(new Plateau("Plateau"+i,new Ressource("Bois"),i));
        }

        cl2.choisirPlateau(cl1.choisirPlateau(listePlateau));

        assertNotEquals(cl2.getPlateauJoueur(),cl1.getPlateauJoueur());
    }


    @Test
    public void testJouer(){
        Client c = new Client("http://127.0.0.1:557");

        ArrayList<Carte> listeCartes = new ArrayList<Carte>();
        for(int i=0;i<7;i++){
            listeCartes.add(new Carte("Carte n" + i,i));
        }

        ArrayList<Plateau> listePlateau= new ArrayList<Plateau>();
        for (int i = 0; i < 10; i++) {
            listePlateau.add(new Plateau("Plateau"+i,new Ressource("Bois"),2));
        }

        c.choisirPlateau(listePlateau);
        c.getPlateauJoueur().ajouterMerveille(new Merveille(12));
        c.setCartesJoueurCourrantes(listeCartes);
        c.jouer();

        assertTrue((c.getPlateauJoueur().getNiveauDeMerveilleActuel() > 0) || (c.getPoints() > 0));
    }
}
