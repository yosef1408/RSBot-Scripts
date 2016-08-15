package supaplex.exceptions;

/**
 * Created by Andreas on 20.07.2016.
 */
public class QueueEmptyException extends RuntimeException {
    public QueueEmptyException() {
        super();
    }

    public QueueEmptyException(String s) {
        super(s);
    }

    public QueueEmptyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public QueueEmptyException(Throwable throwable) {
        super(throwable);
    }
}
