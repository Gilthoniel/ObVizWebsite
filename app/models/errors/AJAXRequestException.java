package models.errors;

import constants.Constants;

import java.io.IOException;

/**
 * Created by gaylor on 08/18/2015.
 */
public class AJAXRequestException extends IOException {
    private static final long serialVersionUID = -2780707329716084750L;

    @Override
    public String getMessage() {

        return Constants.AJAX_REQUEST_EXCEPTION;
    }
}
