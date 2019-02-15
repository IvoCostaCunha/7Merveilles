package commun;

public class Merveille {

		private int pointsMerveille = 0;
		private int niveau;
		private boolean valable;
		
		public Merveille (int pointsMerveille)
		{
			this.valable = false;
			this.pointsMerveille = pointsMerveille;
		}
		public Merveille ()
		{
			this.valable = false;
			pointsMerveille = 0 + (int)	(Math.random() * ((20-0) + 1)); 
		}

		/*---------- Geteurs ----------*/
		public void setMerveilleValable() { valable = true; };
		public Boolean getStatutMerveille() { return valable; };
		public int getPointsMerveille() { return pointsMerveille; }
		public int getNiveauMerveille() { return niveau; }


		/*---------- Seteurs ----------*/
		public void setNiveau(int niveau) { this.niveau = niveau; }

}



