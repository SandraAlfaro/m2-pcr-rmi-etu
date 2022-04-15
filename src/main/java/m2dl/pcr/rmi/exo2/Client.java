package m2dl.pcr.rmi.exo2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class Client implements IClient {

    private UUID uuid;
    private IHM ihm;
    private IServer server;

    private Client(UUID uuid, IServer server, IHM ihm) {
        this.uuid = uuid;
        this.server = server;
        this.ihm = ihm;
    }

    @Override
    public void notifyClient() throws RemoteException {
        this.ihm.addAllMessages(this.server.getAllMsg());
    }

    public static void main(String[] args) {
        IHM ihm = new IHM();
        UUID uuid = UUID.randomUUID();

        // Initialize list of existant msg
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            IServer server = (IServer) registry.lookup("Server");

            IClient client = new Client(uuid, server, ihm);

            IClient clientStub = (IClient) UnicastRemoteObject.exportObject(client,0);
            registry.bind(uuid.toString(),clientStub);

            server.connect(uuid);
            ihm.setVisible(true);

        } catch (Exception ex) {
            System.err.println("Client exception: " + ex.toString());
            ex.printStackTrace();
        }
    }

    private static class IHM extends JFrame implements ActionListener {
        private JButton sendMsgButton;
        private JTextField inputMsg;
        private JTextArea displayMsg;

        public IHM() {
            // Send Message area
            sendMsgButton = new JButton("Send Message");//creating instance of JButton
            sendMsgButton.addActionListener(this);
            inputMsg = new JTextField("Set message here !");
            JPanel sendPannel = new JPanel(new GridLayout(0,2));
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
        }

        public void actionPerformed(ActionEvent ev) {
            try {
                Registry registry = LocateRegistry.getRegistry(1099);
                IServer stub = (IServer) registry.lookup("Server");
                if (ev.getSource() == sendMsgButton) {
                    String msg = inputMsg.getText();
                    stub.addMsg(msg);
                }
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
    }

}
