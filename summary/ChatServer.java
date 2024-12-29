package summary;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;

    public ChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
    }

    public void start() {
        System.out.println("Сервер запущен!");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при работе сервера: " + e.getMessage());
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String userName;
    
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
    
        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
    
                // Получаем имя пользователя от клиента
                userName = reader.readLine();
                broadcast(userName + " присоединился к чату!");
    
                String message;
                while ((message = reader.readLine()) != null) {
                    broadcast(userName + ": " + message);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при обработке клиента: " + e.getMessage());
            } finally {
                clients.remove(this);
                close();
                broadcast(userName + " покинул чат!");
            }
        }
    
        private void broadcast(String message) {
            for (ClientHandler client : clients) {
                client.writer.println(message);
            }
        }
    
        private void close() {
            try {
                socket.close();
                reader.close();
                writer.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer(55555);
            server.start();
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }
}
