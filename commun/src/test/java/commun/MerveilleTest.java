package commun;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MerveilleTest {


	@Test
	public void MerveilleValabletest() throws Exception {
		//fail("Not yet implemented");
		boolean res = true;
		Merveille merv = new Merveille(15);
		merv.setMerveilleValable();
		assertEquals(res,merv.getStatutMerveille());
		
	}

	@Test
	public void  ValeurMerveilletest() throws Exception 
	{
		Merveille merv = new Merveille(20);
		int res = 20;
		assertEquals(res,merv.getPointsMerveille());
	}

	// Test d'une valeur absolue sur une valeur random ... faudra que tu corrige Yan
	/*@Test
	public void ValeuRandomMerveilleTest() throws Exception
	{
		Merveille merv = new Merveille();
		int res = 20;
		assertEquals(res == merv.getPointsMerveille(),false);
	}*/

}
