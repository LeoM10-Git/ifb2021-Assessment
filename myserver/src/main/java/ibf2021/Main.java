package ibf2021;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {


    public static void main(String[] args) throws IOException {
        String userPort = "3000";

        if (args != null && args.length > 0) {
            if (args.length > 1 && "--port".equals(args[0])) {
                userPort = args[1];
                HttpServer httpServer = new HttpServer(userPort, "static/");
                httpServer.startServer();
            } else if (args.length > 1 && "--docRoot".equals(args[0])) {
                String[] listDir = args[1].split(":");
                for (String dir : listDir) {
                    if (checkFile(dir)) {
                        dir = dir +"/";
                        HttpServer httpServer = new HttpServer(userPort, dir);
                        httpServer.startServer();
                        break;
                    } else {
                        System.out.println("Resources file not accessible, server not started.");
                    }
                }
            }
            if (args.length > 1 && "--port".equals(args[0]) && "--docRoot".equals(args[2])) {
                userPort = args[1];
                String[] listDir = args[3].split(":");
                for (String dir : listDir) {
                    if (checkFile(dir)) {
                        dir = dir +"/";
                        HttpServer httpServer = new HttpServer(userPort, dir);
                        httpServer.startServer();
                        break;
                    } else {
                        System.out.println("Resources file not accessible");
                    }
                }
            }
        }else {
            HttpServer httpServer = new HttpServer(userPort, "static/");
            httpServer.startServer();
        }
    }

    public static boolean checkFile(String userDir){
        Path path = Paths.get(userDir);
        File file = path.toFile();
        return file.isDirectory();
    }
}
