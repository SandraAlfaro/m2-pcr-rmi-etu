package m2dl.pcr.rmi.exo2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class Client implements IClient {

    private UUID uuid;
    private static IServer server;
    private IHM ihm;

    private Client(UUID uuid, IServer server, IHM ihm) {
        this.uuid = uuid;
        this.server = server;
        this.ihm = ihm;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void notifyClient() throws RemoteException {
        this.ihm.addAllMessages(this.server.getAllMsg());
    }

    public static void main(String[] args) {
        IHM ihm = new IHM();
        ihm.initIHM();
        ihm.setVisible(true);
    }

    private static class IHM extends JFrame implements ActionListener, WindowListener {

        private static JButton sendMsgButton;
        private static JTextField inputMsg;
        private static JTextArea displayMsg;
        private static JTextField inputRoom;
        private static JButton connectButton;

        private IServer server;
        private UUID clientID;

        public IHM() {}

        public void initIHM() {
            // Send Message area
            sendMsgButton = new JButton("Send Message");//creating instance of JButton
            sendMsgButton.addActionListener(this);
            sendMsgButton.setEnabled(false);
            inputMsg = new JTextField("Set message here !");
            inputMsg.setEnabled(false);
            inputRoom = new JTextField("2");
            connectButton = new JButton("Connect");
            connectButton.addActionListener(this);
            JPanel sendPannel = new JPanel(new GridLayout(2,4));
            sendPannel.add(inputRoom);
            sendPannel.add(connectButton);
            sendPannel.add(inputMsg);
            sendPannel.add(sendMsgButton);

            // Display Messages area
            displayMsg = new JTextArea();

            // Global area
            JPanel ihm = new JPanel(new BorderLayout());
            ihm.add(sendPannel, BorderLayout.PAGE_START);
            ihm.add(displayMsg, BorderLayout.CENTER);

            this.add(ihm);// set panel for layout
            this.setSize(400, 500);//400 width and 500 height
            this.setVisible(false);//making the frame visible
            this.addWindowListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendMsgButton){
                String msg = inputMsg.getText();
                try {
                    this.server.addMsg(msg);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == connectButton){
                connectToRoom();
            }
        }

        public void connectToRoom(){
            inputMsg.setEnabled(true);
            sendMsgButton.setEnabled(true);

            clientID = UUID.randomUUID();
            try {
                Registry registry = LocateRegistry.getRegistry(1099);
                this.server = (IServer) registry.lookup(inputRoom.getText());

                IClient client = new Client(clientID, this.server,this);

                IClient clientStub = (IClient) UnicastRemoteObject.exportObject(client,0);
                registry.bind(clientID.toString(),clientStub);

                this.server.connect(clientID);

            } catch (Exception ex) {
                System.err.println("Client exception: " + ex.toString());
                ex.printStackTrace();
            }
        }

        public void addAllMessages(List<String> msgs) {
            displayMsg.selectAll();
            displayMsg.replaceSelection("");
            for (String msg: msgs) {
                displayMsg.append(msg+"\n");
            }
        }

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            try {
                this.server.disconect(clientID);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}
