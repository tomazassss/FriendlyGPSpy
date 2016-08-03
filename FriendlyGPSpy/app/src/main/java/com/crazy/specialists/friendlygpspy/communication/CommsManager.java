package com.crazy.specialists.friendlygpspy.communication;

import android.util.Log;
import android.view.View;

import com.crazy.specialists.friendlygpspy.communication.client.SocketClient;
import com.crazy.specialists.friendlygpspy.communication.server.SocketServer;
import com.crazy.specialists.friendlygpspy.utils.Displayer;

import java.io.IOException;

import static com.crazy.specialists.friendlygpspy.utils.Parameters.SERVER_PORT;

/**
 * Created by Tomas on 2016.05.29.
 */
public class CommsManager {

    private SocketServer socketServer;
    private Thread serverThread;

    private SocketClient socketClient;
    private Thread clientThread;

    private final String endpointIp;
    private final Displayer displayer;

    public CommsManager(final String endpointIp, final Displayer displayer) {
        Log.w("myApp" , "Creating comms manager" + this);
        this.endpointIp = endpointIp;
        this.displayer = displayer;
    }

    public void start() {
        Log.w("myApp" , "Starting comms manager" + this);
        startServer();
        startClient();
    }

    private void startServer() {
        socketServer = new SocketServer(SERVER_PORT, displayer);
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

    public void clientSendData(String data){
        if (socketClient != null) {
            socketClient.sendData(data);
        }
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
