package assessment.exception;

public class TransactionFailedException extends Exception {
    public TransactionFailedException() {
        super();
    }

    public TransactionFailedException(String msg) {
        super(msg);
    }
}
