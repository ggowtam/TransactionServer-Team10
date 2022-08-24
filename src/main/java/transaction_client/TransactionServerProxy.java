package transaction_client;

import java.io.*;
import java.net.*;
import java.util.*;
import messages.Message;
import constants.MessageTypes;

public class TransactionServerProxy implements MessageTypes {

    String host;
    int port;

    private Socket dbConnection = null;
    private ObjectOutputStream writeTo = null;
    private ObjectInputStream readFrom = null;
    private Integer transId = 0;

    public TransactionServerProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public int openTransaction() {
        try {
            dbConnection = new Socket(host, port);

            writeTo = new ObjectOutputStream(dbConnection.getOutputStream());
            readFrom = new ObjectInputStream(dbConnection.getInputStream());

            // construct an open message
            Message openMessage = new Message(OPEN_TRANSACTION);

            // send open message to server
            writeTo.writeObject(openMessage);

            // server responds with transId
            transId = (Integer) readFrom.readObject();

        } catch (Exception ex) {
            System.out.println("Error occur in TransactionServerProxy");
            ex.printStackTrace();
        }

        return transId;
    }

    // close socket connection
    public void closeTransaction() {
        try {
            // construct a close message
            Message closeMessage = new Message(CLOSE_TRANSACTION);
            // send close message
            writeTo.writeObject(closeMessage);
            // close the connection.
            dbConnection.close();
        } catch (Exception ex) {
            System.out.println("Error occur in TransactionServerProxy");
            ex.printStackTrace();
        }
    }

    public int read(int accountNumber) {
        Integer balance = null;
        Message readMessage = new Message(READ_TRANSACTION, accountNumber);

        try {
            writeTo.writeObject(readMessage);
            balance = (Integer) readFrom.readObject();

        } catch (Exception ex) {
            System.out.println("Error occur in TransactionServerProxy");
            ex.printStackTrace();
        }

        return balance;
    }

    public int write(int accountNumber, int amount) {
        int[] writeArray = {accountNumber, amount};
        Message writeMessage = new Message(WRITE_TRANSACTION, writeArray);

        Integer balance = null;

        try {
            // send the writeMessage
            writeTo.writeObject(writeMessage);
            // open the writeMessage
            balance = (Integer) readFrom.readObject();
        } catch (Exception ex) {
            System.out.println("Error occur in TransactionServerProxy");
            ex.printStackTrace();
        }

        return balance;
    }

}
