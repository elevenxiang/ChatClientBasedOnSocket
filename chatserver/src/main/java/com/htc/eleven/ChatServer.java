package com.htc.eleven;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer extends Service {

    private static final int Port = 12345;
    private String linebreak = System.getProperty("line.separator");

    public ChatServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new SocketServerThread().start();

    }

    private class workThread extends Thread {
        private Socket socket;

        public workThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//                String line;
                while (reader.readLine() != null) {
                    writer.write("I am ok !" + linebreak);
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                ServerSocket socket_s = new ServerSocket(Port);
                while (true) {
                    System.out.println("Waiting client +++");
                    Socket socket_client = socket_s.accept();
                    System.out.println("Waiting client ---");

                    new workThread(socket_client).start();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
