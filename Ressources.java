
public enum Ressources {
	
	ARGILE(0),
	MINERAI(0),
	PIERRE(0),
	BOIS(0),
	VERRE(0),
	TISSU(0),
	PAPYRUS(0),
	PIECE(0);
	
	private int valeur;
	
	Ressources (int value)
	{
		valeur = value;
	}
	
	public int getQuantiteRessources()
	{
		return valeur;
	}
	
	public void setQuantiteRessources(int nouvelleValeur)
	{
		valeur = nouvelleValeur;
	}
	
	/*@Override
	public String toString()
	{
		return this.toString();
	}*/
}

	
