package commun;

/**
 * @author Ivo COSTA CUNHA on 15/02/2019
 * @project 7Merveilles
 */

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class JoueurTest {

    @Test
    public void testInitialisationRessources(){
        Joueur j1= new Joueur(1);
        Ressource r1, r2, r3;
        r1 = j1.getRessourcesJoueur().get(0);
        r2 = j1.getRessourcesJoueur().get(1);
        r3 = j1.getRessourcesJoueur().get(2);

        Ressource tr1 = new Ressource("Bois",0);
        Ressource tr2 = new Ressource("Or",0);
        Ressource tr3 = new Ressource("Pierre",0);

        assertTrue((tr1.estDeMemeType(r1)) && (tr2.estDeMemeType(r2)) && (tr3.estDeMemeType(r3)));
    }

    @Test void testObtenirRessource(){
        Joueur j1 = new Joueur(1);
        j1.obtenirRessource(new Ressource("Bois",1));
        assertEquals(1,j1.getRessourcesJoueur().get(0).getNbRessource());
    }

    @Test
    public void testAjouterPoints(){
        Joueur j1 = new Joueur(1);
        j1.ajouterPoints(5);
        assertEquals(5,j1.getPoints());
    }


    @Test
    public void testRenvoyerCartes(){
        ArrayList<Carte> listeCartes = new ArrayList<Carte>();
        for(int i=0;i<7;i++){
            listeCartes.add(new Carte("Carte n" + i,i));
        }

        Joueur j1 = new Joueur(1);
        Joueur j2 = new Joueur(2);

        j1.setCartesJoueurCourrantes(listeCartes);
        Carte c1 = j1.choisirCarte();

        j2.setCartesJoueurCourrantes(j1.renvoyerCartes());
        Carte c2 = j1.choisirCarte();

        assertEquals(5,j2.renvoyerCartes().size());
    }

    @Test
    public void testChoisirCarte(){
        ArrayList<Carte> listeCartes = new ArrayList<Carte>();
        for(int i=0;i<7;i++){
            listeCartes.add(new Carte("Carte n" + i,i));
        }
        Joueur j1 = new Joueur(1);
        Joueur j2 = new Joueur(2);

        j1.setCartesJoueurCourrantes(listeCartes);
        Carte c1 = j1.choisirCarte();

        j2.setCartesJoueurCourrantes(j1.renvoyerCartes());
        Carte c2 = j1.choisirCarte();

        assertFalse(c1.equals(c2));

    }

    @Test
    public void ajouterCarteUtilise(){
        Joueur j1 = new Joueur(1);
        Carte carteTest = new Carte("test",420);
        j1.ajouterCarteUtilisee(carteTest);
        assertTrue(carteTest.equals(j1.getCarteJoueurUtilisees().get(0)));
    }

    @Test
    public void testChoisirPlateau(){
        Joueur j1 = new Joueur(1);
        Joueur j2 = new Joueur(2);

        ArrayList<Plateau> listePlateau= new ArrayList<Plateau>();
        for (int i = 0; i < 10; i++) {
            listePlateau.add(new Plateau("Plateau"+i,new Ressource("Bois"),i));
        }

        j2.choisirPlateau(j1.choisirPlateau(listePlateau));

        assertNotEquals(j2.getPlateauJoueur(),j1.getPlateauJoueur());
    }

    @Test
    public void testJouer(){
        Joueur j1 = new Joueur(1);

        ArrayList<Carte> listeCartes = new ArrayList<Carte>();
        for(int i=0;i<7;i++){
            listeCartes.add(new Carte("Carte n" + i,i));
        }

        ArrayList<Plateau> listePlateau= new ArrayList<Plateau>();
        for (int i = 0; i < 10; i++) {
            listePlateau.add(new Plateau("Plateau"+i,new Ressource("Bois"),2));
        }

        j1.choisirPlateau(listePlateau);
        j1.getPlateauJoueur().ajouterMerveille(new Merveille(12));
        j1.setCartesJoueurCourrantes(listeCartes);
        j1.jouer();

        assertTrue((j1.getPlateauJoueur().getNiveauDeMerveilleActuel() > 0) || (j1.getPoints() > 0));
    }
}
