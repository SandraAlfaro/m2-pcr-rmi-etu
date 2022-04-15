package m2dl.pcr.rmi.exo2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
    void notifyClient() throws RemoteException;
}
