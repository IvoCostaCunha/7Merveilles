package moteur;

import commun.Carte;
import commun.Merveille;
import commun.Plateau;
import commun.Ressource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivo COSTA CUNHA on 01/04/2019
 * @project 7Merveilles
 */
public class MoteurTest {
    @Test
    public void testIntialiserPlateaux(){
        ArrayList<Plateau> listePlateaux = new ArrayList<Plateau>();
        ArrayList<Plateau> listePlateauxDistribuables = new ArrayList<Plateau>();
        ArrayList<Merveille> listeMerveilles = new ArrayList<Merveille>();
        int nbMerveilleRand = (int)(Math.random()*4);

        Merveille MerveilleUn = new Merveille(1,3,0,"");
        Merveille MerveilleDeux = new Merveille(2,0,2,"");
        Merveille MerveilleTrois = new Merveille(3,7,0,"");

        listeMerveilles.add(MerveilleUn);
        listeMerveilles.add(MerveilleDeux);
        listeMerveilles.add(MerveilleTrois);

        String nomPlateau = "LeColosseDeRhodes";
        Plateau plt = new Plateau(listeMerveilles, nomPlateau, new Ressource("Minerai",1),"A");
        listePlateaux.add(plt);
        Moteur m = new Moteur();
        listePlateauxDistribuables = m.getPlateaux();

        assertEquals(listePlateaux.get(0).getNomPlateau(),listePlateauxDistribuables.get(0).getNomPlateau());
        }
    @Test
    public void testInitialiserCartes()
    {
        ArrayList<ArrayList<Carte>> listemains = new ArrayList<ArrayList<Carte>>();
        ArrayList<ArrayList<Carte>> listeCartesDistibuables = new ArrayList<ArrayList<Carte>>();
        ArrayList<String> typesCartesTest = new ArrayList<String>();
        typesCartesTest.add("pointsCarteCivil");
        typesCartesTest.add("pointsCarteMilitaire");
        typesCartesTest.add("pointsCarteCommercial");
        typesCartesTest.add("pointsCarteScientifique");
        for(int i=0;i<7;i++){
            listemains.add(new ArrayList<Carte>(7));
        }
        for (ArrayList<Carte> cartesDistribuables : listemains)
        {

            int choixTypeCarteAlea = (int) (Math.random() * 4);
            int valeurCarte = (int) (Math.random() * 20);
            String typeCarte = typesCartesTest.get(choixTypeCarteAlea);
            cartesDistribuables.add(new Carte("Carte - 1", typeCarte, valeurCarte));
            Moteur m = new Moteur();
            listeCartesDistibuables = m.getMains();
        }
        assertEquals(listemains.get(0).get(0).getNomCarte(), listeCartesDistibuables.get(0).get(0).getNomCarte());

    }

}

