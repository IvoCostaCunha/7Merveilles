package lanceur;

import client.Client;
import serveur.Serveur;

public class Partie {

    public final static void main(String [] args) {

        Thread serveur = new Thread(new Runnable() {
            @Override
            public void run() {
                Serveur.main(null);
            }
        });

        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                Client.main(null);
            }
        });

        Thread client2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Client.main(null);
            }
        });
        Thread client3 = new Thread(new Runnable() {
            @Override
            public void run() {
                Client.main(null);
            }
        });

        Thread client4 = new Thread(new Runnable() {
            @Override
            public void run() {
                Client.main(null);
            }
        });

        /*---------- Lancement du serveur ----------*/
        serveur.start();

        /*---------- Lancement des clients ----------*/
        client.start();
        client2.start();
        client3.start();
        client4.start();

    }
}
