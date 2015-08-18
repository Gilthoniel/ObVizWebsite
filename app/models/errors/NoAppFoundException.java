package models.errors;

import constants.Constants;

/**
 * Created by gaylor on 07.07.15.
 * Exception if the id is incorrect
 */
public class NoAppFoundException extends Exception {

    private static final long serialVersionUID = 5209626079795986141L;

    @Override
    public String getMessage() {
        return Constants.NO_APP_EXCEPTION;
    }
}
