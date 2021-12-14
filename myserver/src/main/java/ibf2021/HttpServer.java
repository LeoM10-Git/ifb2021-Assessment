package ibf2021;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer{
    String port;
    public HttpServer(String port) {
        this.port = port;
    }

    public void startServer() throws IOException {
        System.out.printf("Listen to port %s: ",port);
        ServerSocket server = new ServerSocket(Integer.parseInt(port));

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        while (true) {
            Socket socket = server.accept();
            HttpClientConnection httpClientConnection = new HttpClientConnection(socket);
            threadPool.submit(httpClientConnection);
        }
    }
}
