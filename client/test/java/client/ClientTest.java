package client;

import java.util.ArrayList;

import client.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ivo COSTA CUNHA on 13/03/2019
 * @project 7Merveilles
 */
public class ClientTest {
    @Test
    public void testCreationConnexion(){
        Client client = new Client("http://127.0.0.1:666");
        assertTrue(client.seConnecter());
    }
}
