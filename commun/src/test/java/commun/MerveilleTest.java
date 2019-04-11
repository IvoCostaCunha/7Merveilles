package commun;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MerveilleTest {


	@Test
	public void MerveilleValabletest() throws Exception {
		//fail("Not yet implemented");
		boolean res = true;
		Merveille merv = new Merveille(1,0,10,"");
		merv.setMerveilleValable();
		assertEquals(res,merv.getStatutMerveille());
		
	}

	@Test
	public void  ValeurMerveilletest() throws Exception 
	{
		Merveille merv = new Merveille(1,0,10,"");
		int res = 20;
		merv.setPointsMerveille(res);
		assertEquals(res,merv.getPointsMerveille());
	}

	
	//@Test
	/*public void ValeuRandomMerveilleTest() throws Exception
	{
		Merveille merv = new Merveille(1);
		int res = 20;
		if (merv.getPointsMerveille() !=20)
		{
			assertEquals(res == merv.getPointsMerveille(),false);
		}
		else 
		{
			assertEquals(res == merv.getPointsMerveille(),true);
		}
		
	}*/

}
