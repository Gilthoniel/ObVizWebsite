package models.errors;

import constants.Constants;

import java.io.IOException;

/**
 * Created by gaylor on 07.07.15.
 */
public class ServerOverloadedException extends IOException {

    @Override
    public String getMessage() {
        return Constants.SERVER_OVERLOADED_EXCEPTION;
    }
}
