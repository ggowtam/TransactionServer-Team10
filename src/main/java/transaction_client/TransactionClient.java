package transaction_client;

import java.util.Properties;
import utils.PropertyHandler;

public class TransactionClient extends Thread {

    private static TransactionServerProxy serverProxy;
    public int numberOfTransactions;
    public static int numberOfAccounts;
    public static int initialBalance;
    public int port;
    public String host;

    public static StringBuffer log;

    public TransactionClient(String clientPropertiesFile, String serverPropertiesFile) {
        Properties serverProperties = new PropertyHandler(serverPropertiesFile);
        Properties clientProperties = new PropertyHandler(clientPropertiesFile);

        host = serverProperties.getProperty("HOST");
        port = Integer.parseInt(serverProperties.getProperty("PORT"));
        numberOfAccounts = Integer.parseInt(serverProperties.getProperty("NUMBER_ACCOUNTS"));
        initialBalance = Integer.parseInt(serverProperties.getProperty("INITIAL_BALANCE"));

        numberOfTransactions = Integer.parseInt(clientProperties.getProperty("NUMBER_TRANSACTIONS"));

        log = new StringBuffer("");
    }

    @Override
    public void run() {
        int index;
        for (index = 0; index < numberOfTransactions; index++) {

            new Thread() {
                @Override
                public void run() {

                    TransactionServerProxy transaction = new TransactionServerProxy(host, port);
                    int transId = transaction.openTransaction();
                    System.out.println("Transaction Number: " + transId + " has STARTED");

                    int accountFrom = (int) Math.floor(Math.random() * numberOfAccounts);
                    int accountTo = (int) Math.floor(Math.random() * numberOfAccounts);
                    int amount = (int) Math.ceil(Math.random() * initialBalance);
                    int balance;

                    System.out.println("Transaction Number: " + transId + " Amount: $" + amount + " Transfered from: "
                            + accountFrom + " to " + accountTo);
                    balance = transaction.read(accountFrom);
                    int balanceAfterDebitedAmount = balance - amount;
                    transaction.write(accountFrom, balanceAfterDebitedAmount);

                    balance = transaction.read(accountTo);
                    int balanceWithCreditAmount = balance + amount;
                    transaction.write(accountTo, balanceWithCreditAmount);

                    transaction.closeTransaction();

                    System.out.println("Transaction Number: " + transId + " COMPLETED");

                }
            }.start();
        }
    }

}
