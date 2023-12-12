package org.bingo.rpc;

import java.io.Serializable;

public class BingoRequest implements Serializable {
    private final int clientID;
    private final int guessedNumber;
    private final GatewayOperation operation;

    public  BingoRequest(int clientID, GatewayOperation operation, int guessedNumber) throws Exception {
        this.clientID = clientID;
        this.operation = operation;
        this.guessedNumber = guessedNumber;
    }

    // Getters

    public int GetClientID() {
        return clientID;
    }

    public int GetGuessedNumber() {
        return guessedNumber;
    }

    public GatewayOperation GetOperationType() { return  operation;}
}
