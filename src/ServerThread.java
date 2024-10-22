import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {

  DataInputStream dataInputStream;
  Server server;
  Socket socket;

  public ServerThread(Socket socket, Server server) {
    try {
      this.dataInputStream = new DataInputStream(socket.getInputStream());
      this.server = server;
      this.socket = socket;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void run() {
    try {
      while (true) {
        String msg = dataInputStream.readUTF().trim();
        if (!msg.isEmpty()) {
          server.sendToAll(msg);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      server.deleteClient(socket);
    }
  }
}
