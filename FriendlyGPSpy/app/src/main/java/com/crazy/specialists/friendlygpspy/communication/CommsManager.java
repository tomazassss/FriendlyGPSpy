package com.crazy.specialists.friendlygpspy.communication;

import android.provider.Contacts;

import com.crazy.specialists.friendlygpspy.communication.client.SocketClient;
import com.crazy.specialists.friendlygpspy.communication.server.SocketServer;
import com.crazy.specialists.friendlygpspy.utils.Utilities;

import java.io.IOException;

/**
 * Created by Tomas on 2016.05.29.
 */
public class CommsManager {

    //TODO: Should be configurable
    private static final int SERVER_PORT = 46761;

    private SocketServer socketServer;
    private Thread serverThread;

    private SocketClient socketClient;
    private Thread clientThread;

    private final String myIp;
    private final String endpointIp;

    public CommsManager(final String endpointIp) {
        this.myIp = Utilities.getIPAddress(true);
        this.endpointIp = endpointIp;
    }

    public void start() {
        startServer();
        startClient();
    }

    private void startServer() {
        socketServer = new SocketServer(SERVER_PORT);
        serverThread = new Thread(socketServer);
        serverThread.start();
    }

    private void startClient() {
        socketClient = new SocketClient(endpointIp, SERVER_PORT);
        clientThread = new Thread(socketClient);
        clientThread.start();
    }

    public void stop() throws IOException {
        if(socketServer != null) {
            socketServer.close();
        }
        if(socketClient != null) {
            socketClient.close();
        }

    }
}
