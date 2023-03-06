package sg.edu.nus.iss.app.assessment.exception;

public class TransactionFailedException extends Exception {
    public TransactionFailedException() {
        super();
    }

    public TransactionFailedException(String msg) {
        super(msg);
    }
}
