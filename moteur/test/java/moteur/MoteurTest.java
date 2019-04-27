package moteur;

import commun.Merveille;
import commun.Plateau;
import commun.Ressource;
import org.junit.jupiter.api.Test;

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
        for (int i=0;i<7;i++) {
            for (int j = 0; j < nbMerveilleRand; j++) {
                listeMerveilles.add(new Merveille((int) (Math.random() * 20)));
            }

            String nomPlateau = "plateau" + (i + 1);
            Plateau plt = new Plateau(listeMerveilles, nomPlateau, new Ressource("Pierre", 5));
            listePlateaux.add(plt);
            Moteur m = new Moteur();
            listePlateauxDistribuables = m.getPlateaux();
        }
        for (int i=0;i<7;i++)
        {
            assertEquals(listePlateaux.get(i).getNomPlateau(),listePlateauxDistribuables.get(i).getNomPlateau());
        }
    }
}
