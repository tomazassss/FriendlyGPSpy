package com.crazy.specialists.friendlygpspy.communication;

import android.content.Context;
import android.view.View;

import com.crazy.specialists.friendlygpspy.communication.client.SocketClient;
import com.crazy.specialists.friendlygpspy.communication.server.SocketServer;

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

    private final String endpointIp;
    private final Context context;

    public CommsManager(final Context cnt, final String endpointIp) {
        this.endpointIp = endpointIp;
        context = cnt;
    }

    public void start() {
        startServer();
        startClient();
    }

    private void startServer() {
        socketServer = new SocketServer(context, SERVER_PORT);
        serverThread = new Thread(socketServer);
        serverThread.start();
    }

    private void startClient() {
        socketClient = new SocketClient(context, endpointIp, SERVER_PORT);
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

    public View.OnClickListener createSendLocationListener() {
        return new SendLocationListener();
    }

    class SendLocationListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (socketClient != null) {
                socketClient.sendData("Location123");
            }
        }
    }
}
