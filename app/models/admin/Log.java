package models.admin;

import models.Date;
import models.ID;

/**
 * Created by gaylor on 15.07.15.
 */
public class Log {

    private ID _id;
    private String machine;
    private int status;
    private int revNo;
    private String appID;
    private Date tstamp;

    public String getMachine() {
        return machine;
    }

    public String getAppID() {
        return appID;
    }

    public int getStatus() {
        return status;
    }

    public int getRevNo() {
        return revNo;
    }

    public Date getTstamp() {
        return tstamp;
    }
}
