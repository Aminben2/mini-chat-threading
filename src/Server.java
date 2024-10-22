import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

public class Server {
  Hashtable<Socket, DataOutputStream> clients = new Hashtable<>();

  public Server(int port) {
    try {
      ServerSocket server = new ServerSocket(port);
      System.out.println("Server started : " +server);
      while (true) {
        Socket socket = server.accept();
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client connected : "+socket);
        clients.put(socket, dataOutputStream);
        new ServerThread(socket, this).start();
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void sendToAll(String msg) {
    for (Map.Entry<Socket, DataOutputStream> entry : clients.entrySet()) {
      DataOutputStream d = entry.getValue();
      try {
        d.writeUTF(msg);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public void deleteClient(Socket s) {
    try {
      synchronized (clients) {
        System.out.println("Client disconnected : "+s);
        clients.remove(s);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
