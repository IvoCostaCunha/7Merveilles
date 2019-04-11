package commun;

public class Merveille {

		private String nomMerveille;
		private int pointsMerveille;
		private int poinsMilitairesMerveille;
		private String effetMerveille;
		private boolean valable;
		private int coutsRessourcesMerveilles;
		private int numMerveille;
		
		public Merveille (int numMerveille,int pointsMerveille,int pointsMilitairesMerveille,String effetMerveille)
        {
			this.valable = false;
			this.numMerveille = numMerveille;
			this.nomMerveille = "Merveille num" + numMerveille;
			this.effetMerveille = effetMerveille;
			//pointsMerveille = 0 + (int)	(Math.random() * ((20-0) + 1));
			this.pointsMerveille = pointsMerveille;
		}

		/*---------- Geteurs ----------*/
		public void setMerveilleValable() { valable = true; };
		public String getNomMerveille() { return nomMerveille; }
		public Boolean getStatutMerveille() { return valable; };
		public int getPointsMerveille() { return pointsMerveille; }
		public int getPointsMilitairesMerveille() {return poinsMilitairesMerveille;}
		public int getNumMerveille(){ return numMerveille;}


		/*---------- Seteurs ----------*/
		public void setNomMerveille(String nom) { this.nomMerveille = nom;}
		public void setPointsMerveille(int points) { this.pointsMerveille = points; }
		public void setPoinsMilitairesMerveille(int pointsMilitairesMerveille) { this.poinsMilitairesMerveille=pointsMilitairesMerveille;}
		public void setNumMerveille(int numMerveille) { this.numMerveille = numMerveille;}

}



