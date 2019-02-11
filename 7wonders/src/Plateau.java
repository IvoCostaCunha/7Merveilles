
public enum Plateau {
	LeColossedeRohdes(Ressources.MINERAI),
	LePhareDAlexandrie(Ressources.VERRE),
	LeTempleDArtemis(Ressources.PAPYRUS),
	LesJardinsSuspendusDeBabylone(Ressources.ARGILE),
	LaStatueDeZeusAOlympie(Ressources.BOIS),
	LeMausoleeDHalicarnasse(Ressources.TISSU),
	LaGrandePyramideDeGizeh(Ressources.PIERRE);
	
	private Ressources RessourcePlateau;
	
 Plateau (Ressources uneRessource)
{
	uneRessource.setQuantiteRessources(1);
	RessourcePlateau = uneRessource; 
	
}
 
 public Ressources getRessourcePlateau()
 {
	 return this.RessourcePlateau;
 }
 
}
