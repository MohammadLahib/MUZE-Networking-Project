package Home;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Server {


    static ArrayList<MyFile> myFiles = new ArrayList<>();


    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }


    public void startServer() {

        int fileid = 0;
        JFrame jFrame = new JFrame("MUZE Server");
        jFrame.setSize(750, 750);
        jFrame.setLocationRelativeTo(null);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JScrollPane jScrollPane = new JScrollPane(jPanel);

        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel jlTitle = new JLabel("Server audio receiver");
        JLabel jlPort = new JLabel("Server Port Number: " + serverSocket.getLocalPort());

        jlPort.setFont(new Font("Arial", Font.BOLD, 15));
        jlPort.setBorder(new EmptyBorder(25, 0, 5, 0));
        jlPort.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlPort.setForeground(Color.red);

        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));

        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jFrame.add(jlTitle);
        jFrame.add(jlPort);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);



        try {
            while (!serverSocket.isClosed()) {
                System.out.println("Server stared successfully.");

                Socket socket = serverSocket.accept();
                System.out.println("A new Client has connected!");

//                A thread to handle each and every client
                ClientInstances clientInstances =  new ClientInstances(socket);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());


                int audionamelength = dataInputStream.readInt();

                if(audionamelength > 0){
                    byte[] audionamebytes = new byte[audionamelength];
                    dataInputStream.readFully(audionamebytes, 0, audionamebytes.length);
                    String audioName =  new String(audionamebytes);

                    int contentlength =  dataInputStream.readInt();

                    if(contentlength > 0){
                        byte[] contentbytes =  new byte[contentlength];
                        dataInputStream.readFully(contentbytes, 0, contentlength);

                        JPanel jpFileRow = new JPanel();
                        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));

                        JLabel jlFileName =  new JLabel(audioName);
                        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
                        jlFileName.setBorder(new EmptyBorder(10, 0, 10, 0));

                        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

                        if (getFileExtension(audioName).equalsIgnoreCase("mp3")) {
                            jpFileRow.setName(String.valueOf(fileid));
                            jpFileRow.addMouseListener(getMyMouseListener());

                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);
                            jFrame.validate();
                        } else {
                            jpFileRow.setName(String.valueOf(fileid));
                            jpFileRow.addMouseListener(getMyMouseListener());

                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);

                            jFrame.validate();

                        }
                        myFiles.add(new MyFile(fileid, audioName, contentbytes, getFileExtension(audioName)));

                        fileid++;

                    }


                }

//                creating a thread for each client and start this thread
                clientInstances.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    when errors occurred then close the server socket
    public void closeServerSocket(){
        try {
            if (serverSocket !=null){
                serverSocket.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        System.out.println("Please Provide the Port Number in which the server would be Listening: ");
        Scanner reader = new Scanner(System.in);
        int port = reader.nextInt();

        ServerSocket serverSocket =  new ServerSocket(port);
        Server server = new Server(serverSocket);
        server.startServer();


    }



    public static MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jpanel  = (JPanel) e.getSource();

                int filedId = Integer.parseInt(jpanel.getName());

                for (MyFile myFile: myFiles ){
                    if (myFile.getId() == filedId){
                        JFrame jfPreview = createFrame(myFile.getName(), myFile.getData(), myFile.getFileExtension());
                        jfPreview.setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }




    public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {
        JFrame jFrame = new JFrame("MUZE Audios");
        jFrame.setSize(400, 400);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        JLabel jlTitle = new JLabel();
        jlTitle. setAlignmentX(Component.CENTER_ALIGNMENT);
        jlTitle.setFont (new Font("Arial", Font.BOLD, 25));
        jlTitle. setBorder ( new EmptyBorder(20, 0, 10, 0));
        JLabel jlPrompt = new JLabel("Are you sure you want to download the audio" + fileName);
        jlPrompt.setFont(new Font("Arial", Font.BOLD, 20));
        jlPrompt.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton jbYes = new JButton("Yes");
        jbYes.setPreferredSize(new Dimension(150, 75));
        jbYes.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbNo = new JButton("No");
        jbNo.setPreferredSize(new Dimension(150, 75));
        jbNo.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel jlFilecontent = new JLabel();

        jlFilecontent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButtons  = new JPanel();

        jpButtons.setBorder(new EmptyBorder(20, 0, 10, 0));

        jpButtons.add(jbYes);
        jpButtons.add(jbNo);

        if (fileExtension.equalsIgnoreCase("mp3")){
            jlFilecontent.setText("<html>" + new String(fileData) + "</html>");

        } else {
            jlFilecontent.setIcon(new ImageIcon(fileData));
        }

        jbYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileToDownload = new File(fileName);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                    fileOutputStream.write(fileData);
                    fileOutputStream.close();

                    jFrame.dispose();
                }catch (IOException error){
                    error.printStackTrace();
                }
            }
        });

        jbNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
            }
        });

        jPanel.add(jlTitle);
        jPanel.add(jlPrompt);
        jPanel.add(jlFilecontent);
        jPanel.add(jpButtons);

        jFrame.add(jPanel);

        return jFrame;

    }





        public static String getFileExtension(String fileName){

        int i = fileName.lastIndexOf('.');

        if (i>0){
            return fileName.substring(i+1);
        }else {
            return "no extension found";
        }
    }
}
