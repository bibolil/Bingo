package org.bingo.rpc;
import org.apache.xmlrpc.XmlRpcException;
import java.util.List;

public interface BingoInterface {
    int Play(int clientID, int guessedNumber) throws XmlRpcException;
    int GetBestScore(int clientID) throws XmlRpcException;
    List<Integer> GetClientHistory(int clientID) throws  XmlRpcException;
}