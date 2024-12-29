package summary;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ChatClient extends JFrame {
    private JTextArea chatHistory;
    private JTextField messageField;
    private JButton sendButton;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String userName;

    public ChatClient(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);

        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        add(new JScrollPane(chatHistory), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        messageField = new JTextField(30);
        sendButton = new JButton("Отправить");

        inputPanel.add(messageField);
        inputPanel.add(sendButton);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void connectToServer() {
        try {
            socket = new Socket("localhost", 55555);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
    
            // Отправляем имя пользователя на сервер при подключении
            writer.println(userName);
    
            Thread receiverThread = new Thread(new MessageReceiver());
            receiverThread.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка подключения к серверу");
        }
    }
    

    public void disconnectFromServer() {
        try {
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка отключения от сервера");
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            writer.println(message);
            //appendToChatHistory("Вы: " + message);
            messageField.setText("");
        }
    }

    private void appendToChatHistory(String message) {
        chatHistory.append(message + "\n");
        chatHistory.setCaretPosition(chatHistory.getDocument().getLength());
    }

    private class MessageReceiver implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    appendToChatHistory(message);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(ChatClient.this, "Соединение прервано");
            } finally {
                disconnectFromServer();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClient client = new ChatClient("Чат-клиент");

            // Запрашиваем имя пользователя
            String userName = JOptionPane.showInputDialog(client, "Введите ваше имя:");
            if (userName == null || userName.trim().isEmpty()) {
                System.exit(0); // Выходим из приложения, если пользователь не ввел имя
            }
            client.setUserName(userName);

            client.setVisible(true);

            // Подключаемся к серверу при запуске клиента
            client.connectToServer();
        });
    }
}
