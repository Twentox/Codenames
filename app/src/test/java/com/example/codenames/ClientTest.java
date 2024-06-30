package com.example.codenames;

import com.example.codenames.socketNetwork.Client;
import com.example.codenames.socketNetwork.DataTypes;
import com.example.codenames.socketNetwork.PlayerClient;
import com.example.codenames.GameEngine.GameRoles;
import java.io.IOException;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        Client client = new PlayerClient("John", GameRoles.ERMITTLER,new Socket("localhost",8888));
        client.sendData(client.getDataOutputStream(),"hello", DataTypes.COMMAND.getType());

        /** Empfängerreihenfolge
         *
         * 1. String DataTypes.HEADER als String geschickt
         * 2. ArtefactID | Prüfung in NetworkInformation checkHeader(ArtefactID,Version)
         * 3. Version    |
         * 4. Datatypes.COMMAND
         * 5. Befehl an sich
         */
    }
}
