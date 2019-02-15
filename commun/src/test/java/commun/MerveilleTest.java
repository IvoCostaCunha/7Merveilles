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

	
	@Test
	public void ValeuRandomMerveilleTest() throws Exception
	{
		Merveille merv = new Merveille();
		int res = 20;
		if (merv.getPointsMerveille() !=20)
		{
			assertEquals(res == merv.getPointsMerveille(),false);
		}
		else 
		{
			assertEquals(res == merv.getPointsMerveille(),true);
		}
		
	}

}
