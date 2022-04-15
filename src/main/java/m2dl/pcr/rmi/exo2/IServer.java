package m2dl.pcr.rmi.exo2;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface IServer extends Remote {
    void addMsg(String msg) throws RemoteException;
    List<String> getAllMsg() throws RemoteException;
    void connect(UUID clientID) throws RemoteException, NotBoundException;
    void disconect(UUID clientID) throws RemoteException;
}
