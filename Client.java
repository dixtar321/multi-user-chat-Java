import java.io.*;
import java.net.*;


// Совместное рисование в сетке 3х3. Пример команды ‘@draw 1 2 X’. 
//В ячейку с адресом в первом столбце и второй строке добавляется Х. 
//Сетка отрисовывает автоматически после каждого изменения (в консоли).


public class Client{
    private Socket socket;

    private DataInputStream in;
    private DataOutputStream out;
    private BufferedReader keyboard;
    private Thread listener;    

    public Client(String adr, int port) throws IOException {
        InetAddress ipAddress = InetAddress.getByName(adr); // создаем объект который отображает вышеописанный IP-адрес
        socket = new Socket(ipAddress, port); // создаем сокет используя IP-адрес и порт сервера
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        keyboard = new BufferedReader(new InputStreamReader(System.in));
        listener = new Thread(new FromServer());
    }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);// порт, к которому привязывается сервер
        String address = "127.0.0.1";// это IP-адрес компьютера, где исполняется наша серверная программа.
        new Client(address, port).run();
    }

    private void socketClose() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {//отправляет на сервер
        try {
            while (true) {
                System.out.println("write your login:");
                String line = keyboard.readLine();
                out.writeUTF(line);
                out.flush();
                if(confirm()){//если пароль верный
                    listener.start();
                    break;
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        try {
            while (true) {
                String line;
                line = keyboard.readLine();
                if (socket.isClosed())
                    break;
                out.writeUTF(line); // отсылаем серверу
                out.flush();
                if (line.equals("@quit")) {
                    socketClose();
                    break;
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private boolean confirm() throws IOException {
        String line = in.readUTF();
        System.out.println(line);
        if (line.equals("Welcome"))
            return true;
        return false;
    }

    private class FromServer implements Runnable {//принимает сообщения

        public void run() {
            try {
                while (true) {
                    String line;
                    line = in.readUTF(); // ждем пока сервер отошлет строку текста
                    System.out.println(line);
                }
            } catch (IOException e) {
                socketClose();
            } finally {
                socketClose();
            }
        }
    }

}