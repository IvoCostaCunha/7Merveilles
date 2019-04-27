package commun;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ivo COSTA CUNHA on 15/02/2019
 * @project 7Merveilles
 */

public class PlateauTest {

    @Test
    public void testAjoutMerveille(){
        Ressource r1 = new Ressource("Bois",1);
        Merveille m = new Merveille(1,10,10,"");

        Plateau p = new Plateau("p1",r1,3);
        p.ajouterMerveille(m);

        Merveille mBis = p.getListeMerveilles().get(0);
        assertEquals(m,mBis);
    }

    @Test
    public void testConstruireMerveille(){
        Ressource r1 = new Ressource("Bois",1);
        Plateau p = new Plateau("p1",r1,3);

        Merveille m = new Merveille(1,10,10,"");
        Merveille m2 = new Merveille(1,10,10,"");
        Merveille m3 = new Merveille(1,10,10,"");

        p.ajouterMerveille(m,m2,m3);

        Carte c1 = new Carte("test", "type", 1);

        while(p.construireMerveilleSuivante(c1)){
            p.construireMerveilleSuivante(c1);
        }

        assertEquals(3,p.getNiveauDeMerveilleActuel());
    }
}
