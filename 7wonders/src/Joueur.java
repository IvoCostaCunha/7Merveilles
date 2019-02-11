import java.util.ArrayList;

public class Joueur {
	private String name;
	private Plateau PlateauJoueur;
	private ArrayList<Ressources> RessourcesJoueur = new ArrayList<Ressources>();
	private ArrayList<Carte> CartesJoueur =  new ArrayList<Carte>();
	
	
	public Joueur(int value)
	{
		name = "Joueur" + value;
		for (Ressources r : Ressources.values())
		{
			RessourcesJoueur.add(r);
		}
	}
	
	public void obtenirPlateau(Plateau unPlateau)
	{
		PlateauJoueur = unPlateau;
		this.setNouvelleRessourceJoueur(PlateauJoueur.getRessourcePlateau());
	}
	
	public void setNouvelleRessourceJoueur(Ressources uneRessource)
	{
		for (Ressources r : RessourcesJoueur)
		{
			if (uneRessource == r)
			{
				r.setQuantiteRessources(uneRessource.getQuantiteessources());
			}
		}
	}
	
	
}

