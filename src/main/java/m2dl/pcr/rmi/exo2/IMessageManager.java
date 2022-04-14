package m2dl.pcr.rmi.exo2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IMessageManager extends Remote {
    void addMsg(String msg) throws RemoteException;

    List<String> getAllMsg() throws RemoteException;
}
