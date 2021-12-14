package ibf2021;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args != null && args.length > 0) {
            String port = args[0];
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        System.out.println("Listen to port 3000: ");
        int defaultPort = 3000;
        ServerSocket server = new ServerSocket(defaultPort);

        while (true) {
            Socket socket = server.accept();
            HttpClientConnection httpClientConnection = new HttpClientConnection(socket);
            threadPool.submit(httpClientConnection);
        }
    }
}
