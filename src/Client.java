import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;

public class Client extends JFrame implements Runnable {
  JTextField input;
  JTextArea chat;
  String name;

  DataInputStream dataInputStream;
  DataOutputStream dataOutputStream;
  Socket s;

  public Client(String name, String host, int port) {
    super("Chat - " + name);
    setSize(400, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    setLayout(new BorderLayout());

    chat = new JTextArea();
    chat.setEditable(false);
    chat.setLineWrap(true);
    chat.setWrapStyleWord(true);
    chat.setBackground(new Color(245, 245, 245));
    chat.setFont(new Font("SansSerif", Font.PLAIN, 14));
    chat.setMargin(new Insets(10, 10, 10, 10));

    JScrollPane chatScroll = new JScrollPane(chat);
    add(chatScroll, BorderLayout.CENTER);

    JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    input = new JTextField();
    input.setFont(new Font("SansSerif", Font.PLAIN, 14));
    input.setMargin(new Insets(5, 5, 5, 5));
    inputPanel.add(input, BorderLayout.CENTER);

    JButton sendButton = new JButton("Send");
    sendButton.setPreferredSize(new Dimension(80, 30));
    sendButton.setBackground(new Color(0, 123, 255));
    sendButton.setForeground(Color.WHITE);
    sendButton.setFocusPainted(false);
    inputPanel.add(sendButton, BorderLayout.EAST);

    add(inputPanel, BorderLayout.SOUTH);

    ActionListener sendAction =
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            String msg = input.getText().trim();
            if (!msg.isEmpty()) {
              try {
                dataOutputStream.writeUTF(name + ": " + msg);
                input.setText("");
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        };

    input.addActionListener(sendAction);
    sendButton.addActionListener(sendAction);

    try {
      s = new Socket(host, port);
      dataInputStream = new DataInputStream(s.getInputStream());
      dataOutputStream = new DataOutputStream(s.getOutputStream());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          this, "Connection Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    new Thread(this).start();

    setVisible(true);
  }

  @Override
  public void run() {
    while (true) {
      try {
        chat.append(dataInputStream.readUTF() + "\n");
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
