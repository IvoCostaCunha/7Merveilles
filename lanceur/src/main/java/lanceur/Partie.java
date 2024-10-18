package lanceur;

import client.Client;
import com.corundumstudio.socketio.Configuration;
import serveur.Serveur;

public class Partie {

    public final static void main(String [] args) {

        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(8001);

        Serveur s = new Serveur(config);
        s.démarrer();

        /* Instanciations des clients en cas normal */
        Client [] c = new Client[2];
        for(int i = 0; i < c.length; i++)  {
            c[i] = new Client("http://127.0.0.1:8001");
            c[i].seConnecter();
        }

        /* Pour les tests */
        /*Client c= new Client("http://127.0.0.1:557");
        c.seConnecter();*/

        /* Instanciation d'un client pour simplifier les tests */
        //Client unClient = new Client("http://127.0.0.1:557");
        //unClient.seConnecter();

    }
}
