package com.crazy.specialists.friendlygpspy.communication.server;

import android.content.Context;

import com.crazy.specialists.friendlygpspy.utils.Utilities;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tomas on 2016.05.29.
 */
public class SocketServer implements Runnable, Closeable {

    private ServerSocket serverSocket;
    private final Context context;
    private final int serverPort;
    private boolean isStarted;

    public SocketServer(final Context cnt , final int port) {
        this.serverPort = port;
        context = cnt;
    }

    @Override
    public void run() {
        isStarted = true;

        Socket socket = null;
        try {
            this.serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!Thread.currentThread().isInterrupted() && isStarted) {
            try {
                socket = serverSocket.accept();

                CommunicationThread commThread = new CommunicationThread(socket);
                new Thread(commThread).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws IOException {
        if(isStarted) {
            isStarted = false;
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String locationRepresentation = input.readLine();
                    //TODO: Parse Location from string
                    Utilities.showToast(context, locationRepresentation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
