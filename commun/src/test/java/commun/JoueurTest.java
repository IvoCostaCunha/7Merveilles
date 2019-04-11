package commun;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ivo COSTA CUNHA on 01/04/2019
 * @project 7Merveilles
 */
public class JoueurTest {
    @Test
    public void testInitialisationRessources(){
        Joueur j = new Joueur();
        Ressource r1, r2, r3;
        r1 = j.getRessources().get(0);
        r2 = j.getRessources().get(1);
        r3 = j.getRessources().get(2);

        Ressource tr1 = new Ressource("Bois",0);
        Ressource tr2 = new Ressource("Or",0);
        Ressource tr3 = new Ressource("Pierre",0);

        assertTrue((tr1.estDeMemeType(r1)) && (tr2.estDeMemeType(r2)) && (tr3.estDeMemeType(r3)));
    }

    @Test void testObtenirRessource(){
        Joueur j = new Joueur();
        j.obtenirRessource(new Ressource("Bois",1));
        assertEquals(1,j.getRessources().get(0).getNbRessource());
    }

    @Test
    public void testAjouterPoints(){
        Joueur j = new Joueur();
        j.ajouterPoints(5);
        assertEquals(5,j.getPoints());
    }


    @Test
    public void testRenvoyerCartes(){
        ArrayList<Carte> listeCartes = new ArrayList<Carte>();
        for(int i=0;i<7;i++){
            listeCartes.add(new Carte("Carte n" + i,i));
        }

        Joueur j1 = new Joueur();
        Joueur j2 = new Joueur();

        j1.setMain(listeCartes);
        Carte c1 = j1.choisirCarte();

        j2.setMain(j1.renvoyerCartes());
        Carte c2 = j2.choisirCarte();

        assertEquals(5,j2.renvoyerCartes().size());
    }

    @Test
    public void testChoisirCarte(){
        ArrayList<Carte> listeCartes = new ArrayList<Carte>();
        for(int i=0;i<7;i++){
            listeCartes.add(new Carte("Carte n" + i,i));
        }
        Joueur j1 = new Joueur();
        Joueur j2 = new Joueur();

        j1.setMain(listeCartes);
        Carte c1 = j1.choisirCarte();

        j2.setMain(j1.renvoyerCartes());
        Carte c2 = j2.choisirCarte();

        assertFalse(c1.getNomCarte().equals(c2.getNomCarte()));

    }

    @Test
    public void ajouterCarteJoue(){
        Joueur j = new Joueur();
        Carte carteTest = new Carte("test",420);
        j.ajouterCarteJoue(carteTest);
        assertTrue(carteTest.equals(j.getCartesJouees().get(0)));
    }
/*
    @Test
    public void testChoisirPlateau(){
        Joueur j1 = new Joueur();
        Joueur j2 = new Joueur();;

        ArrayList<Plateau> listePlateau= new ArrayList<Plateau>();
        for (int i = 0; i < 10; i++) {
            listePlateau.add(new Plateau("Plateau"+i,new Ressource("Bois"),i));
        }

        j2.choisirPlateau(j2.choisirPlateau(listePlateau));

        assertNotEquals(j2.getPlateau(),j1.getPlateau());
    }


    @Test
    public void testJouer(){
        Joueur j = new Joueur();

        ArrayList<Carte> listeCartes = new ArrayList<Carte>();
        for(int i=0;i<7;i++){
            listeCartes.add(new Carte("Carte n" + i,i));
        }

        ArrayList<Plateau> listePlateau= new ArrayList<Plateau>();
        for (int i = 0; i < 10; i++) {
            listePlateau.add(new Plateau("Plateau"+i,new Ressource("Bois"),2));
        }

        j.choisirPlateau(listePlateau);
        j.getPlateau().ajouterMerveille(new Merveille(1,10,10,""));
        j.setMain(listeCartes);
        j.jouer();

        assertTrue((j.getPlateau().getNiveauDeMerveilleActuel() > 0) || (j.getPoints() > 0));
    }*/
}
