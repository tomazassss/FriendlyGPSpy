package com.crazy.specialists.friendlygpspy.communication.client;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Tomas on 2016.05.29.
 */
public class SocketClient implements Runnable, Closeable{

    private final String ip;
    private final int port;
    private final Context context;

    private Socket socket;

    public SocketClient(Context context, final String ip, final int port) {
        this.ip = ip;
        this.port = port;
        this.context = context;
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

    public void sendData(final String data) {
        if(socket != null) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                out.println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
