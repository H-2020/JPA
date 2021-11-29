package be.intec.services.exceptions;

import be.intec.services.models.entities.*;
import java.util.Arrays;

public class MessageException extends RuntimeException {

    public MessageException(String message) {
        super(message);
    }

    public MessageException notFound() {
        return new MessageException("Message not found");
    }

    public MessageException alreadyExists() {
        return new MessageException("Message already exists");
    }

    public MessageException requiredFields(String... fields) {
        return new MessageException("Required fields: " + Arrays.toString(fields));
    }

    public MessageException nullMessageException() {
        return new MessageException("Message cannot be null");
    }

}
