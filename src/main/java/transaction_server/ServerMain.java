package transaction_server;

import utils.PropertyHandler;
import java.util.Properties;

public class ServerMain {

    public static void main(String[] args) {

        TransactionServer transactionServer = new TransactionServer("serverProperties.txt");

        transactionServer.start();

    }

}
