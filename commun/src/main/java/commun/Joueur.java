/*package commun;

import java.util.ArrayList;

public class Joueur {
	private String name;
	private int Points;
	private Plateau PlateauJoueur;
	private ArrayList<Ressources> RessourcesJoueur = new ArrayList<Ressources>();
	private ArrayList<Carte> CartesJoueur =  new ArrayList<Carte>();
	private int nombreCarte = CartesJoueur.size();
	
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
	
	public void compterLesPointsJoueur(int desPoints)
	{
		Points += desPoints;
	}
	
	// Fonction potentiellement utilisé par le serveur ??
	public int getLesPointsJoueur()
	{
		return Points;
	}
	public Plateau getPlateauJoueur()
	{
		return PlateauJoueur;
	}
	public void setPlateauJoueur(Plateau nouveauPlateau)
	{
		this.PlateauJoueur = nouveauPlateau;
	}
	
	public ArrayList <Carte> getCarteJoueur()
	{
		return CartesJoueur;
	}
	public ArrayList<Ressources> getRessourcesJoueur()
	{
		return RessourcesJoueur;
	}
	public void utiliserCarteSurMerveille(Carte uneCarte,int valeurMerveille)
	{
		nombreCarte--;
		Merveille maMerveille = new Merveille(valeurMerveille);
		maMerveille.setMerveilleValable();
	}
}*/
