package models.errors;

import constants.Constants;

/**
 * Created by gaylor on 07.07.15.
 * Exception if the id is incorrect
 */
public class NoAppFoundException extends Exception {

    @Override
    public String getMessage() {
        return Constants.NO_APP_EXCEPTION;
    }
}
