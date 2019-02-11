import java.util.ArrayList;

public class Joueur {
	private String name;
	private int Points;
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
			if (uneRessource.toString() == r.toString())
			{
				r.setQuantiteRessources(uneRessource.getQuantiteRessources());
			}
		}
	}
	
	public void compterLesPoints(int desPoints)
	{
		Points += desPoints;
	}
	
	
	
}

