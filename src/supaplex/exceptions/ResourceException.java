package supaplex.exceptions;

/**
 * Created by Andreas on 29.06.2016.
 */
public class ResourceException extends RuntimeException {

    public ResourceType resourceType;

    public ResourceException() {
        super();
    }

    public ResourceException(String s, ResourceType resourceType) {
        super(s);
        this.resourceType = resourceType;
    }

    public ResourceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ResourceException(Throwable throwable) {
        super(throwable);
    }
}
