package transactions;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import constants.MessageTypes;
import transaction_server.TransactionServer;
import messages.Message;

import java.util.*;
import java.io.IOException;

public class TransactionManager implements MessageTypes {

    // open one connection per transaction and keeps it open until its closed
    static final ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    static int transactionCounter = 0;
    static boolean applyLocking = false;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public TransactionManager(boolean applyLocking) {
        this.applyLocking = applyLocking;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void runTransaction(Socket client) {
        (new TransactionManagerWorker(client)).start();
    }

//    needs a lot more logic in this class (i.e. handle READ, WRITE, OPEN and CLOSE messages)
    public class TransactionManagerWorker extends Thread {

        Socket client = null;
        ObjectInputStream readFrom = null;
        ObjectOutputStream writeTo = null;

        Transaction transaction = null;
        int accountNumber = 0;
        int balance = 0;

        int total = TransactionServer.accountMgr.getBranchTotal();
        boolean continueLooping = true;
        Message message = null;

        public TransactionManagerWorker(Socket client) {
            this.client = client;

            // streams set up
            try {
                readFrom = new ObjectInputStream(client.getInputStream());
                writeTo = new ObjectOutputStream(client.getOutputStream());

            } catch (IOException e) {
                System.out.println("TransactionManagerWorker failed to open streams");
                e.printStackTrace();
                System.exit(1);
            }

        }

        @Override
        public void run() {
            while (continueLooping) {
                try {
                    message = (Message) readFrom.readObject();

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(" TransactionManagerWorker run() cannot read object");
                    System.exit(1);

                }

                // gets type of message
                // compares to what kind of message
                switch (message.getType()) {
                    case OPEN_TRANSACTION:
					synchronized (transactions) {
                            // create a new transaction
                            transaction = new Transaction(transactionCounter++);
                            // add it to the rest fo the transactions
                            transactions.add(transaction);
                        }

                        try {
                            // write to the object
                            writeTo.writeObject(transaction.getID());

                        } catch (IOException e) {
                            System.out.println("TransactionManagerWorker OPEN_TRANSACTION experienced error when reading");
                        }

                        transaction.log("[TransactionManagerWorker.run] OPEN_TRANSACTION >>>>>> Transaction Number: "
                                + transaction.getID());
                        break;
                    case READ_TRANSACTION:
                        // get the content of the message
                        accountNumber = (Integer) message.getContent();
                        // read the balance from the acccount manager
                        balance = TransactionServer.accountMgr.readBalance(accountNumber, transaction, applyLocking);
                        try {
                            // the the balance
                            writeTo.writeObject((Integer) balance);
                        } catch (IOException e) {
                            System.out.println("TransactionManagerWorker READ_TRANSACTION experienced error when reading");

                        }

                        System.out.println("[TransactionManagerWorker.run] READ_TRANSACTION >>>>>>");
                        System.err.println(ANSI_RED + "Account Number: "
                                + accountNumber + " Balance: " + balance + ANSI_RESET);

                        break;
                    case WRITE_TRANSACTION:
                        // create an object
                        int[] content = (int[]) message.getContent();
                        // read account number from content
                        accountNumber = content[0];
                        // read the balance from content
                        balance = content[1];
                        // get balance from the account
                        balance = TransactionServer.accountMgr.writeBalance(accountNumber, transaction, balance,
                                applyLocking);

                        try {
                            // write the balance
                            writeTo.writeObject((Integer) balance);
                        } catch (IOException e) {
                            System.out.println("TransactionManagerWorker WRITE_TRANSACTION experienced error when writing");

                        }

                        System.out.println("[TransactionManagerWorker.run] WRITE_TRANSACTION  >>>>>> ");
                        System.err.println("Account Number: "
                                + accountNumber + " New Balance: " + balance);

                        break;
                    case CLOSE_TRANSACTION:
                        // unlock the transaction
                        TransactionServer.lockMgr.unlock(transaction);
                        // remove the transaction
                        transactions.remove(transaction);

                        try {
                            // close everything opened
                            readFrom.close();
                            writeTo.close();
                            client.close();
                            continueLooping = false;
                        } catch (IOException e) {
                            System.out.println("TransactionManagerWorker CLOSE_TRANSACTION experienced error when closing");
                        }
                        transaction.log("[TransactionManagerWorker.run] CLOSE_TRANSACTION  >>>>>> Transaction: "
                                + transaction.getID());

                        if (TransactionServer.transView) {
                            System.out.println(transaction.getLog());
                        }
                        System.out.println("-----------BRANCH TOTAL--------$" + total);
                        break;

                }

            }

        }

    }
}
