package commun;

/**
 * @author Ivo COSTA CUNHA on 15/02/2019
 * @project 7Merveilles
 */

/**
 * Classe qui représente les ressouces et qui contient differents outils de comptage / Comparaison de ressources
 */
public class Ressource {

    private String nomRessource;
    private int nbRessource;

    /**
     * Constructeur de la classe Ressource avec param nb
     * @param nomRessource nom de la ressouce
     * @param nbRessource nombre de la ressource
     */
    public Ressource (String nomRessource, int nbRessource){
        this.nomRessource = nomRessource;
        this.nbRessource = nbRessource;
    }

    /**
     * Constructeur de la classe Ressource avec initialisation du nombre a 0
     * @param nomRessource nom de la ressource
     */
    public Ressource (String nomRessource){
        this.nomRessource = nomRessource;
        nbRessource = 0;
    }

    /*---------- Geteurs ----------*/
    private String getNomRessource(){ return nomRessource; }
    public int getNbRessource(){ return nbRessource; }

    /**
     * Retourne true si le type de la ressource de uneRessource est le même que l'instance
     * @param uneRessource objet Ressource avec qui teste
     * @return true ou false
     */
    public Boolean estDeMemeType(Ressource uneRessource){
        return this.getNomRessource().equals(uneRessource.getNomRessource());
    }

    /**
     * Méthode qui incremente le nb une Ressource
     * @param incrementation valeur de l'incrementation
     */
    public void incrementerRessource(int incrementation){
        this.nbRessource += incrementation;
    }

    /**
     * Méthode qui incremente le nb une Ressource de 1
     */
    public void incrementerRessource(){
        this.nbRessource += 1;
    }


}
