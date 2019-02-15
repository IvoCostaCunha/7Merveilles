package commun;

/**
 * @author Ivo COSTA CUNHA on 15/02/2019
 * @project 7Merveilles
 */

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires de la classe Ressource
 */
public class RessourceTest {

    @Test
    public void testComparaisonTypeRessource(){
        Ressource bois = new Ressource("Bois",5);
        Ressource bois2 = new Ressource("Bois");

        assertEquals(true,bois.estDeMemeType(bois2));
    }

    @Test
    public void testIncrementationRessourceSimple(){
        Ressource ressource1 = new Ressource("r1",2);
        ressource1.incrementerRessource();
        assertEquals(3, ressource1.getNbRessource());
    }

    @Test
    public void testIncrementationRessourceAvecParam(){
        Ressource ressource1 = new Ressource("r1",2);
        ressource1.incrementerRessource(2);
        assertEquals(4, ressource1.getNbRessource());
    }






}