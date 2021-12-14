package ibf2021;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {


    public static void main(String[] args) throws IOException {
        String userPort = "3000";
        if (args != null && args.length > 0) {
            if ("--port".equals(args[0]) && args.length < 2) {
                userPort = args[1];
            }
        }
        HttpServer httpServer = new HttpServer(userPort);
        httpServer.startServer();
    }
}
