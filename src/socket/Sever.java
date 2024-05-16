package socket;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Sever {
    private ArrayList<ClientHander> clients = new ArrayList<>();

    public Sever() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8088);
        System.out.println("Server started");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                ClientHander clientHander = new ClientHander(in, out, clients);
                clients.add(clientHander);
                // Start the server handler to send messages from the server
                new Thread(() -> {
                    while (true) {
                        BufferedReader serverReader = new BufferedReader(new InputStreamReader(System.in));
                        try {
                            String serverMessage = serverReader.readLine();
                            if (serverMessage != null) {
                                // Prepend the server's name to the message
                                String formattedMessage = "Server: " + serverMessage;
                                clientHander.send(formattedMessage);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
                socket.close();
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Sever();
    }
}
