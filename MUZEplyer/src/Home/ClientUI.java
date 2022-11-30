
package Home;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

import static Home.Server.createFrame;

public class ClientUI extends javax.swing.JFrame {

    public ClientUI() throws SQLException {
        initComponents("");
    }
    public ClientUI(String uname) throws SQLException {
        initComponents(uname);
    }

    static ArrayList<MyFile> myFiles = new ArrayList<>();


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents(String uname) throws SQLException {



        final File[] fileToSend = new File[1];

        Connection con1;
        PreparedStatement pst;

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

        System.out.println("name is " + uname);

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
        j2.setBackground(Color.cyan);
        j2.setBorder(BorderFactory.createSoftBevelBorder(0));

        JScrollPane jScrollPane = new JScrollPane(j2);

        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        JLabel txtChoose = new JLabel("Click on the File you Want to Download or to Stream");
        txtChoose.setPreferredSize(new Dimension(520,50));
        txtChoose.setFont(new Font("Arial", Font.BOLD, 20));
        txtChoose.setForeground(Color.black);
        txtChoose.setBorder(BorderFactory.createSoftBevelBorder(1));
        txtChoose.setAlignmentX(Component.CENTER_ALIGNMENT);
        j2.add(txtChoose);

        con1 = DbConnect.getConnection();


        Statement st = con1.createStatement();

        String sql = "select * from audio";

        ResultSet rs = st.executeQuery(sql);

        while (rs.next()){
            String fileName = rs.getString("fileName");
            String filePath = rs.getString("filePath");

            byte[] fileNameByte = fileName.getBytes();
            String name = rs.getString("name");
            JLabel jname = new JLabel(name);

            String tbData[] = {fileName, filePath, name};


            JLabel FileName =  new JLabel(fileName);
            FileName.setFont(new Font("Arial", Font.BOLD, 20));
            FileName.setBorder(new EmptyBorder(50, 0, 0, 0));

            FileName.setAlignmentX(Component.CENTER_ALIGNMENT);
            j2.add(FileName);
            j2.add(jname);

        }





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
                Connection con;
                PreparedStatement pst1;
                if (fileToSend[0] == null){
                    jlFileName.setText("please choose a file");
                } else{
                    try {

                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                        Socket socket = new Socket("localhost", 5000);

                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());


                        byte[] fileContentBytes = new byte[(int) fileToSend[0].length()];
                        fileInputStream.read(fileContentBytes);
                        String fileName = fileToSend[0].getName();
                        String filePath = fileToSend[0].getPath();
                        byte[] fileNameByte = fileName.getBytes();


                        dataOutputStream.writeInt(fileNameByte.length);
                        dataOutputStream.write(fileNameByte);

                        con = DbConnect.getConnection();
                        pst1 = con.prepareStatement("insert into audio(fileName, filePath, name) values(?,?,?)");
                        pst1.setString(1, fileName);
                        pst1.setString(2, filePath);
                        pst1.setString(3, uname);
                        pst1.executeUpdate();
                        pst1.close();
                        con.close();

                        dataOutputStream.writeInt(fileContentBytes.length);
                        dataOutputStream.write(fileContentBytes);




                    }catch (IOException | SQLException error){
                        System.out.println("");
                    }
                }
            }
        });

        jFrame.add(jlTitle);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        jFrame.setVisible(true);



    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ClientUI().setVisible(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
