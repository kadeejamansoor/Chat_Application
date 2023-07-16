package Resourse.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//Extending the Thread class in the Client class allows to create
// separate thread of execution for each client in this chat
public class Client extends Thread {
    private ArrayList<Client> clients;
    //    represents the socket connection for the current client
    private Socket socket;
    //    This variable is used to read incoming messages from the client.
    private BufferedReader reader;
    //This variable is used to send messages to the client.
    private PrintWriter writer;

    public Client(Socket socket, ArrayList<Client> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                //If the received message is "exit", the loop breaks, and the thread terminates.
                if (msg.equalsIgnoreCase("exit")) {
                    System.out.println("Client left chat..............");
                    break;
                }
                for (Client cl : clients) {
                    cl.writer.println(msg);
                }
                System.out.println(msg);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            //In the finally block, the BufferedReader, PrintWriter, and socket are closed
        } finally {
            try {
                reader.close();
                writer.close();
                socket.close();
                System.out.println("Client left chat..............");

            } catch (IOException e) {
                // e.printStackTrace();
            }
        }

    }

}