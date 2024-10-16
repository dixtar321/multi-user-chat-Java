import java.net.*;
import java.io.*;

import java.util.concurrent.ConcurrentHashMap;
public class Server{
    private ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap<>();
    private ServerSocket ss;
    private Draw commonDraw;


    public Server(int port) throws IOException {
        ss = new ServerSocket(port);
        this.commonDraw = new Draw();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("I'm working...");
        int port = Integer.parseInt(args[0]);
        new Server(port).run();
    }

    public void run() {
        while (true) {
            try {
                Socket socket = ss.accept();    //подсоединение
                System.out.println("New client");
                ServerClient client = new ServerClient(socket, clients, commonDraw);
                client.start(); //запускаем
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}