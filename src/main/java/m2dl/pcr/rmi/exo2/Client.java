package m2dl.pcr.rmi.exo2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private Client() {}

    private static class IHM extends JFrame implements ActionListener {
        private JButton sendMsgButton;
        private JTextField inputMsg;
        private JButton displayMsgButton;
        private JTextArea displayMsg;

        public IHM() {
            // Send Message area
            sendMsgButton = new JButton("Send Message");//creating instance of JButton
            sendMsgButton.setBounds(130, 100, 100, 40);//x axis, y axis, width, height
            sendMsgButton.addActionListener(this);
            inputMsg = new JTextField("Set message here !");
            JPanel sendPannel = new JPanel(new GridLayout(0,2));
            sendPannel.add(inputMsg);
            sendPannel.add(sendMsgButton);

            // Display Messages area
            displayMsgButton = new JButton("Display Messages");//creating instance of JButton
            displayMsgButton.setBounds(130, 100, 100, 40);//x axis, y axis, width, height
            displayMsgButton.addActionListener(this);
            displayMsg = new JTextArea();
            JPanel displayPannel = new JPanel(new GridLayout(2,0));
            displayPannel.add(displayMsgButton);
            displayPannel.add(displayMsg);

            // Global area
            JPanel ihm = new JPanel(new GridLayout(2,0));
            ihm.add(sendPannel);
            ihm.add(displayPannel);

            this.add(ihm);// set panel for layout
            this.setSize(400, 500);//400 width and 500 height
            this.setVisible(true);//making the frame visible
        }

        public void actionPerformed(ActionEvent ev)
        {
            try {
                Registry registry = LocateRegistry.getRegistry(1099);
                IMessageManager stub = (IMessageManager) registry.lookup("MsgManager");
                if (ev.getSource() == sendMsgButton) {
                    String msg = inputMsg.getText();
                    stub.addMsg(msg);
                }
                if (ev.getSource() == displayMsgButton) {
                    for (String msg: stub.getAllMsg()) {
                        displayMsg.append(msg);
                    }
                }
            } catch (Exception ex) {
                System.err.println("Client exception: " + ex.toString());
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new IHM();
    }
}
