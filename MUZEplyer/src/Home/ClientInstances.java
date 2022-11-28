package Home;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/* A ClientInstances class to handle multiple client request concurrently on the server*/

public class ClientInstances extends Thread {

    private Thread thread;
    Socket socket;

    public ClientInstances(Socket socket){
        this.socket = socket;
    }

    public void start(){
        if(thread == null){
            this.thread = new Thread(this);
        }
        thread.start();
    }

    public void run(){
        try{

            System.out.println("Client request from "+socket.getInetAddress()+":"+socket.getPort()+" connected successfully");

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

//            dos.writeUTF(Server.message+Server.instruction);

            dataOutputStream.flush();


            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        }
        catch(Exception e){
            System.out.println("Client from "+socket.getInetAddress()+":"+socket.getPort()+" has Disconnected");
        }
    }

}