package supaplex.exceptions;

/**
 * Created by Andreas on 30.06.2016.
 */
public class BankException extends RuntimeException {
    public BankException() {
        super();
    }

    public BankException(String s) {
        super(s);
    }

    public BankException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BankException(Throwable throwable) {
        super(throwable);
    }
}
