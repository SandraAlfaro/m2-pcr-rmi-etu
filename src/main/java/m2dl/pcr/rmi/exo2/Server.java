package m2dl.pcr.rmi.exo2;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server implements IServer {
    // 1server/room

    private int room;
    private List<String> msgList;
    private Map<UUID, IClient> clientsList;

    public Server(int room) {
        this.msgList = new ArrayList<>();
        this.clientsList = new HashMap<>();
        this.room = room;
    }

    public static void main(String args[]) {
        try {
            Server obj = new Server(2);
            IServer stub = (IServer) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(String.valueOf(2), stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void addMsg(String msg) throws RemoteException {
        this.msgList.add(msg);
        for(IClient client: clientsList.values()){
            client.notifyClient();
        }
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
