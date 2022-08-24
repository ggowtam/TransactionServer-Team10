package accounts;

import java.util.ArrayList;

import transaction_server.TransactionServer;
import transactions.Transaction;
import constants.LockTypes;
import locks.LockType;

//this class works on accounts which has read & write method , it performs initialization of accounts and acts as access provider for accounts
public class AccountManager implements LockTypes {

    private ArrayList<Account> accountsList;

    public AccountManager(int numAccounts, int initialBalance) {
        accountsList = new ArrayList<Account>();
        for (int index = 0; index < numAccounts; index++) {
            accountsList.add(new Account(index, initialBalance));
        }
    }

    public Account getAccount(int accountNumber) {
        return accountsList.get(accountNumber);
    }

    // reads the balance of an account
    public int readBalance(int accountNumber, Transaction trans, boolean applyLocking) {
        // takes the corresponding account
        Account acc = getAccount(accountNumber);

        // read lock acquired
        if (applyLocking) {
            (TransactionServer.lockMgr).setLock(acc, trans, new LockType(READ_LOCK));
        }

        // account balance is returned
        return acc.getAccountBalance();
    }

    // sets the balance of an account (accountNumber) to newBalance
    public int writeBalance(int accountNumber, Transaction trans, int newBalance, boolean applyLocking) {
        // takes corresponding account
        Account acc = getAccount(accountNumber);

        // write lock acquired
        if (applyLocking) {
            (TransactionServer.lockMgr).setLock(acc, trans, new LockType(WRITE_LOCK));
        }

        // account balance is set to new account
        acc.setAccountBalance(newBalance);

        // account balance is returned
        return newBalance;
    }

    public int getBranchTotal() {
        int branchTotal = this.accountsList.stream().mapToInt(ob -> ob.getAccountBalance()).reduce(0,
                (summationElement1, summationElement2) -> summationElement1 + summationElement2);

        return branchTotal;
    }

}
