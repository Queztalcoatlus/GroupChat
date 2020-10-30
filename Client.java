package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //Connect to the server
        Socket socket = new Socket(InetAddress.getLocalHost(),8000);

        //Start the thread that sends out messages
        SendMessageThread st = new SendMessageThread(socket);
        st.start();
        //Start the thread that receives messages
        ReceiveMessageThread rt = new ReceiveMessageThread(socket);
        rt.start();

        try {
            st.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rt.quit();

        //Wait for receiving thread to finish before closing the socket
        try {
            rt.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        socket.close();

    }

}
