package accounts;

//this class contains Account information like account number and account balance
public class Account {
	private int accountNumber;
	private int accountBalance;

	public Account(int accountNumber, int accountBalance) {
		this.accountNumber = accountNumber;
		this.accountBalance = accountBalance;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public int getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(int accBalance) {
		this.accountBalance = accBalance;
	}

}
