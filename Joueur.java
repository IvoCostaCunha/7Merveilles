import java.util.ArrayList;

public class Joueur {
	private String name;
	private Plateau PlateauJoueur;
	private ArrayList<Ressources> RessourcesJoueur = new ArrayList<Ressources>();
	private ArrayList<Carte> CartesJoueur =  new ArrayList<Carte>();
	
	
	public Joueur(int value)
	{
		name = "Joueur" + value;
	}
	
}

