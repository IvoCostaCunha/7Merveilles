package commun;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
	
	@Test
	public void ValeuRandomMerveilletest() throws Exception 
	{
		Merveille merv = new Merveille();
		int res = 20;
		assertEquals(res == merv.getPointsMerveille(),false);
	}

}
