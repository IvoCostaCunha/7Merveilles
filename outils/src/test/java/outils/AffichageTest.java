package outils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ivo COSTA CUNHA on 12/03/2019
 * @project 7Merveilles
 */
class AffichageTest {
    @Test
    public void testSetCouleur(){
        Affichage aff = new Affichage("GREEN");
        assertTrue(aff.getCouleur() == "\u001B[32m");
    }
}