package m2dl.pcr.rmi.exo2;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server implements IServer {

    private List<String> msgList;
    private Map<UUID, IClient> clientsList;

    public Server() {
        this.msgList = new ArrayList<>();
        this.clientsList = new HashMap<>();
    }

    public static void main(String args[]) {
        try {
            Server obj = new Server();
            IServer stub = (IServer) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("Server", stub);

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

    @Override
    public void connect(UUID clientID) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        IClient client = (IClient) registry.lookup(clientID.toString());
        this.clientsList.put(clientID, client);
        client.notifyClient();
    }

    @Override
    public void disconect(UUID clientID) throws RemoteException {
        this.clientsList.remove(clientID);
    }
}
