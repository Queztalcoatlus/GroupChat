package chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;

public class Server {
    public static void main(String[] args) throws IOException {
        //Start server
        ServerSocket server = new ServerSocket(8000);

        while (true){
            Socket socket = server.accept();
            String clientIp = socket.getInetAddress().getHostAddress();
            System.out.println(clientIp + "login succeeded");
            onlineSockets.add(socket);
            TalkThread tt = new TalkThread(socket);
            tt.start();
        }
    }
    private static HashSet<Socket> onlineSockets = new HashSet<>();
    private static class TalkThread extends Thread{
        private Socket socket;
        private String ip;

        public TalkThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            //When the current talk thread starts running,
            // tell other clients that the current thread is online
            ip = socket.getInetAddress().getHostAddress();
            sendMessage(ip+ " is online");

            try{
                InputStream inputStream = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);
                while(true){
                    String message = br.readLine();
                    sendMessage(ip + ": " + message);
                    if ("exit".equalsIgnoreCase(message)){
                        sendMessage(ip + " has logged off");
                        onlineSockets.remove(socket);
                        break;
                    }
                }
            } catch (IOException e)
            {
                sendMessage(ip + " is encountering connection issues");
                onlineSockets.remove(socket);
            }

        }
        private void sendMessage(String message){
            Iterator<Socket> iterator = onlineSockets.iterator();
            while(iterator.hasNext()){
                Socket client = iterator.next();
                try{
                    OutputStream outputStream = client.getOutputStream();
                    PrintStream ps = new PrintStream(outputStream);
                    ps.println(message);

                } catch (IOException e){
                    // Remove the client if the client is no longer connected
                    iterator.remove();
                }
            }
        }
    }
}
