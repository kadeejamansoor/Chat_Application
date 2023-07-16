import Resourse.controller.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMain {
    private static ArrayList<Client> users = new ArrayList<Client>();


    public static void main(String[] args) throws IOException {
        final int PORT = 2500;
        System.out.println("Server is start..........!");
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is Running..........!");
        Socket accept;
        while (true) {
            accept = serverSocket.accept();
            System.out.println("New Client Connected...");
            System.out.println("-------------------------------------");
            Client userThread = new Client(accept, users);
            users.add(userThread);
            userThread.start();

        }


    }
}
