import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

enum ConnectionMode {Send, Get};

public class Server
{
    int port;

    public Server(int port) {
        this.port = port;
    }

    public String run(ConnectionMode mode, String message) {
        Socket socket;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server ready");
            socket = serverSocket.accept();
            System.out.println("Connected!");

            if (mode.equals(ConnectionMode.Send)) {
                SendMessage(socket, message);
            } else if (mode.equals(ConnectionMode.Get)) {
                return GetMessage(socket);
            }

        } catch (Exception ex) {
            System.out.println("Client left the chat");
        }
        return "Sent";
    }

    static String GetMessage(Socket socket) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String clientMessage = "";
        do {
            int clientChar = bufferedReader.read();
            if (clientChar != 46)
                clientMessage += (char)clientChar;
            else
                break;
        } while (socket.isConnected());
        socket.close();
        System.out.println("Client: " + clientMessage);
        return clientMessage;
    }

    static void SendMessage(Socket socket, String serverMessage) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        for (int i = 0; i < serverMessage.length(); i++) {
            writer.write(serverMessage.charAt(i) - 1);
        }
        writer.flush();
        System.out.println("Server: " + serverMessage);
        socket.close();
    }
}
