package commun;

public class Merveille {

		private String nomMerveille;
		private int pointsMerveille = 0;
		private int niveau;
		private boolean valable;
		
		public Merveille (int numMerveille)
		{
			this.valable = false;
			this.nomMerveille = "Merveille num" + numMerveille;
			pointsMerveille = 0 + (int)	(Math.random() * ((20-0) + 1)); 
		}

		/*---------- Geteurs ----------*/
		public void setMerveilleValable() { valable = true; };
		public String getNomMerveille() { return nomMerveille; }
		public Boolean getStatutMerveille() { return valable; };
		public int getPointsMerveille() { return pointsMerveille; }
		public int getNiveauMerveille() { return niveau; }


		/*---------- Seteurs ----------*/
		public void setNomMerveille(String nom) { this.nomMerveille = nom; }
		public void setNiveau(int niveau) { this.niveau = niveau; }
		public void setPointsMerveille(int points) { this.pointsMerveille = points; }

}



