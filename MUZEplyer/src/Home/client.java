package Home;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class client {

    static ArrayList<MyFile> myFiles = new ArrayList<>();


    public static void main(String[] args){


        final File[] fileToSend = new File[1];

        JFrame jFrame = new JFrame("MUZE Client PLATFORM");
        jFrame.setSize(750, 750);
        jFrame.setLocationRelativeTo(null);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        JLabel jlTitle = new JLabel("MUZE Music Uploader");
        jlTitle.setFont(new Font("Arial",Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jlFileName = new JLabel("CHOOSE A MUSIC TO UPLOAD");
        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
        jlFileName.setBorder(new EmptyBorder(50, 0 , 0, 0));
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButton =  new JPanel();
        jpButton.setBorder(new EmptyBorder(75, 0, 10, 0));

        JButton jbSendFile = new JButton("Upload File");
        jbSendFile.setPreferredSize(new Dimension(120, 75));
        jbSendFile.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbChooseFile = new JButton("Choose a File");
        jbSendFile.setPreferredSize(new Dimension(150, 75));
        jbSendFile.setFont(new Font("Arial", Font.BOLD, 20));



        jpButton.add(jbSendFile);
        jpButton.add(jbChooseFile);

//        JPanel j1 = new JPanel();
//        j1.setPreferredSize(new Dimension(600,20));
//        j1.setBackground(Color.pink);
//        j1.setBorder(BorderFactory.createSoftBevelBorder(1));


        JPanel j2 = new JPanel();
        j2.setBounds(70, 150, 500, 200);
        j2.setPreferredSize(new Dimension(100,200));
        j2.setBackground(Color.white);
        j2.setBorder(BorderFactory.createSoftBevelBorder(0));

        JScrollPane jScrollPane = new JScrollPane(j2);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        JLabel txtChoose = new JLabel("Click on the File you Want to Download or to Stream");
        txtChoose.setPreferredSize(new Dimension(520,50));
        txtChoose.setFont(new Font("Arial", Font.BOLD, 20));
        txtChoose.setForeground(Color.BLUE);
        txtChoose.setBorder(BorderFactory.createSoftBevelBorder(1));
        txtChoose.setAlignmentX(Component.CENTER_ALIGNMENT);
        j2.add(txtChoose);



        jFrame.add(jScrollPane);
        jFrame.setVisible(true);

        




        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser =  new JFileChooser();
                jFileChooser.setDialogTitle("Choose a file to upload");

                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    jlFileName.setText("The file that you want to send is: " + fileToSend[0].getName());

                }
            }
        });

        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileToSend[0] == null){
                    jlFileName.setText("please choose a file");
                } else{
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                        Socket socket = new Socket("localhost", 5000);

                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                        String fileName = fileToSend[0].getName();
                        byte[] fileNameByte = fileName.getBytes();

                        byte[] fileContentBytes = new byte[(int) fileToSend[0].length()];
                        fileInputStream.read(fileContentBytes);

                        dataOutputStream.writeInt(fileNameByte.length);
                        dataOutputStream.write(fileNameByte);

                        dataOutputStream.writeInt(fileContentBytes.length);
                        dataOutputStream.write(fileContentBytes);
                    }catch (IOException error){
                        System.out.println("The server is down!!!");
                    }
                }
            }
        });

        jFrame.add(jlTitle);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        jFrame.setVisible(true);


    }
}
