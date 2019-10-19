package dac.reksio.secretary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final String EXCEPTION_MSG = "Resource not found";

    public ResourceNotFoundException() {
        super(EXCEPTION_MSG);
    }

}
