package transactions;

import java.util.ArrayList;
import transaction_server.TransactionServer;

import locks.Lock;

/*
 * Transaction Class
 *  - [] read
 *  - [] write
 *  - [] close
 */
public class Transaction {

    int transID;
    ArrayList<Lock> locks = null;
    StringBuffer log = new StringBuffer("");

    Transaction(int transID) {
        this.transID = transID;
        this.locks = new ArrayList<Lock>();
    }

    public ArrayList<Lock> getLocks() {
        return locks;
    }

    public int getID() {
        return transID;
    }

    public void addLock(Lock lock) {
        locks.add(lock);
    }

    public StringBuffer getLog() {
        return log;
    }

    public void log(String logString) {
        log.append("\n").append(logString);
        if (!TransactionServer.transView) {
            System.out
                    .println("Transaction Number " + this.getID() + ((this.getID() < 10) ? " " : "") + " " + logString);

        }

    }

}
