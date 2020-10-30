package chat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SendMessageThread extends Thread {
    private Socket socket;

    public SendMessageThread(Socket socket) {
        this.socket = socket;
    }

    public void run(){
        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintStream ps = new PrintStream(outputStream);
            Scanner input = new Scanner(System.in);
            while(true){
                System.out.println("Please enter the message you want to send:");
                String message = input.nextLine();
                ps.println(message);
                if("exit".equalsIgnoreCase(message)){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
