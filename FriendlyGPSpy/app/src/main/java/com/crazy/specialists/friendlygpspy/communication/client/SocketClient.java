package com.crazy.specialists.friendlygpspy.communication.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Tomas on 2016.05.29.
 */
public class SocketClient implements Runnable, Closeable{

    private final String ip;
    private final int port;

    private Socket socket;

    public SocketClient(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            InetAddress serverAddr = InetAddress.getByName(ip);
            socket = new Socket(serverAddr, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if(socket != null)
        {
            socket.close();
        }
    }
}
