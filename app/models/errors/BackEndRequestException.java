package models.errors;

import java.io.IOException;

/**
 * Created by gaylor on 08/26/2015.
 *
 */
public class BackEndRequestException extends IOException {
    private static final long serialVersionUID = 349423930483300093L;

    public BackEndRequestException(String message) {
        super(message);
    }
}
