package commun;

public class Merveille {
	
		private int pointsMerveille = 0;
		private int niveau;
		private boolean valable;
		//private Joueur joueur = new Joueur(1);
		//private boolean partieTerminee = true;
		
		public Merveille (int valeur)
		{
			this.valable = false;
			pointsMerveille = valeur;
		}
		public Merveille ()
		{
			this.valable = false;
			pointsMerveille = 0 + (int)	(Math.random() * ((20-0) + 1)); 
		}
		public void setMerveilleValable()
		{
			valable = true;
		}
		public Boolean getStatutMerveille()
		{
			return valable;
		}
		public int getPointsMerveille()
		{
			return pointsMerveille;
		}
		/*
		public void compterPointsMerveille()
		{
			if (partieTerminee)
				{
					if (valable == true)
					{
						//joueur.compterLesPointsJoueur(pointsMerveille);
					}
			}
		}
		*/
}



