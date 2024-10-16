import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerClient extends Thread{
    private ConcurrentHashMap<String, Socket> allClients;
    private Socket socket;
    private String myName = "";
    public DataInputStream in;
    public DataOutputStream out;
    private Draw drawer;

    public ServerClient(Socket s, ConcurrentHashMap<String, Socket> list, Draw commonDraw) {
        socket = s;
        allClients = list;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawer = commonDraw;
    }

    public void run() {
        try {//сначала проверить логин
            while (true) {
                String login;
                login = in.readUTF();
                out.writeUTF("Welcome");
                myName = login;
                break;
                }
                
            
            while (true) {
                allClients.put(myName, socket);//добавили себя в список
                String line;
                line = in.readUTF();
                if (line.contains("@quit")) {
                    sendAll(myName + " is quited");
                    allClients.remove(myName);
                    break;
                } else if (line.contains("@senduser")) {//отправляем кому-то
                    int end = line.indexOf(" ", "@senduser".length() + 1);//находим конец имени
                    if (end > line.length() || end == -1)//пустая строка
                        out.writeUTF("Empty string is not allowed.");
                    else {
                        String name = line.substring("@senduser".length() + 1, end);//имя получателя
                        line = line.substring(end + 1);
                        //поиск клиента в списке
                        if (allClients.containsKey(name))
                            new DataOutputStream(allClients.get(name).getOutputStream()).writeUTF(myName + ": " + line);
                        else
                            out.writeUTF(name + " is not online.");
                    }
                }

                else if (line.contains("@draw")) {  //рисование
                    String[] parts = line.split(" ");
                    //String command = parts[0]; 
                    int arg1 = Integer.parseInt(parts[1]);
                    int arg2 = Integer.parseInt(parts[2]);
                    char arg3 = parts[3].charAt(0); 

                    if ((arg1 >= 0) && (arg1 <= 2) && (arg2 >= 0) && (arg2 <= 2) && ((arg3 == 'X') || (arg3 == '0'))) {
                        if (drawer.board_insert(arg1, arg2, arg3)) {
                            sendAll(myName + ":" + "\n");
                            sendAll_andMe(drawer.get_board()); // Отправить всем клиентам текущее состояние сетки
                            if (drawer.win_check()) {
                                sendAll_andMe(myName + ": " + "win");
                            }
                        } else {
                            new DataOutputStream(allClients.get(myName).getOutputStream()).writeUTF("Error. The cell is already occupied");
                        }
                    } else {
                        new DataOutputStream(allClients.get(myName).getOutputStream()).writeUTF("Error in typing arguments. Try again (@draw 1 2 X)");
                    }

                }
                else sendAll(myName + ": " + line);
            }
        } catch (IOException e) {//socket closed
        }
    }

    private void sendAll(String line) {//отправляет всем

        for (Socket s : allClients.values())
            if (!s.equals(socket))
                try {
                    new DataOutputStream(s.getOutputStream()).writeUTF(line);
                } catch (IOException e) {
                    // e.printStackTrace();
                }
    }
    private void sendAll_andMe(String line) {//отправляет всем

        for (Socket s : allClients.values())
                try {
                    new DataOutputStream(s.getOutputStream()).writeUTF(line);
                } catch (IOException e) {
                    // e.printStackTrace();
                }
    }
}