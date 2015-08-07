package models;

import java.io.Serializable;

/**
 * Created by gaylor on 02.07.15.
 * ID of an object
 */
public class ID implements Serializable {

    private static final long serialVersionUID = 195537653499647943L;
    private String $oid;

    public String getValue() {
        return $oid;
    }
}
