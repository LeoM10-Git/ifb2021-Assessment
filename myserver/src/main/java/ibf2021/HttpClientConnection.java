package ibf2021;


import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpClientConnection implements Runnable {
    private Socket socket;
    private String dir;
    private DataOutputStream outToClient;
    private BufferedReader inFromClient;

    public HttpClientConnection(Socket socket, String dir) {
        this.socket = socket;
        this.dir = dir;
    }

    @Override
    public void run() {
        System.out.println("Client Connected");
        try {
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToClient = new DataOutputStream(socket.getOutputStream());
            String headerLine = inFromClient.readLine();

            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();
            String httpQueryString = tokenizer.nextToken();

            if (httpMethod.equals("GET")) {
                if (httpQueryString.equals("/")) {
                    // The default home page
                    sendResponse(200, "index.html", true);
                } else {
                    String fileName = httpQueryString.replaceFirst("/", "");
                    boolean isFile = checkFile(fileName);
                    if (isFile) {
                        sendResponse(200, fileName, true);
                    } else {
                        sendResponse(404, String.format("<b>HTTP/1.1 404 Not Found \r\n\r\n <b>%s not found</b>", fileName), false);
                    }
                }
            } else sendResponse(404, "<b>HTTP/1.1 404 Not Found \r\n\r\n not found</b>", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendResponse(int statusCode, String responseString, boolean isFile) throws Exception {

        String statusLine;
        String fileName = null;
        String contentTypeLine = "Content-Type: text/html\r\n";
        FileInputStream fin = null;

        if (statusCode == 200)
            statusLine = "HTTP/1.1 200 OK" + "\r\n";
        else
            statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

        if (isFile) {
            fileName = dir + responseString;
            fin = new FileInputStream(fileName);
            if (fileName.endsWith(".png")){
                contentTypeLine = "Content-Type: img/png \r\n";
            }
        }

        outToClient.writeBytes(statusLine);
        outToClient.writeBytes(contentTypeLine);
        outToClient.writeBytes("Connection: close\r\n");
        outToClient.writeBytes("\r\n");
        if (isFile) sendFile(fin, outToClient);
        else outToClient.writeBytes(responseString);
        outToClient.close();
    }

    public void sendFile(FileInputStream fin, DataOutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while (-1 != (bytesRead = fin.read(buffer))) {
            out.write(buffer, 0, bytesRead);
            out.flush();
        }
        fin.close();
    }

    public boolean checkFile(String fileName) {
        fileName = dir + fileName;
        File checkFile = new File(fileName);
        return checkFile.isFile();
    }
}
