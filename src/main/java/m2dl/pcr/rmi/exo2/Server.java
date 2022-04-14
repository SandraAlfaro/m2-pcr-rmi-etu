package m2dl.pcr.rmi.exo2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server implements IMessageManager {

    private List<String> msgList;

    public Server() {
        this.msgList = new ArrayList<>();
    }

    public static void main(String args[]) {
        try {
            Server obj = new Server();
            IMessageManager stub = (IMessageManager) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("MsgManager", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void addMsg(String msg) {
        this.msgList.add(msg);
    }

    @Override
    public List<String> getAllMsg() {
        return this.msgList;
    }
}
