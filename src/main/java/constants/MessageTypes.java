package constants;

//the various types used are Open Transaction, Read Transaction, Write Transaction, Close Transaction
public interface MessageTypes {

    public final int OPEN_TRANSACTION = 0;
    public final int READ_TRANSACTION = 1;
    public final int WRITE_TRANSACTION = 2;
    public final int CLOSE_TRANSACTION = 3;
}
