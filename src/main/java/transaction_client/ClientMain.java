package transaction_client;

public class ClientMain {

    public static void main(String[] args) {
        (new TransactionClient("clientProperties.txt", "serverProperties.txt")).start();
    }

}
