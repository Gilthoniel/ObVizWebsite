package models.admin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by gaylor on 10/13/2015.
 * Bugs report for the android app
 */
public class Report {

    public String id;
    public String phoneModel;
    public String androidVersion;
    public String appVersionName;
    public int appVersionCode;
    public long totalMemSize;
    public long availableMemSize;
    public String startDate;
    public String crashDate;
    public String stack;

    public JsonObject crashConfiguration = new JsonObject();
    public JsonObject sharedPreferences = new JsonObject();
    public JsonObject display = new JsonObject();
    public JsonObject initialConfiguration = new JsonObject();

    public Report(JsonObject json) {

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            switch (entry.getKey()) {
                case "_id":
                    id = entry.getValue().getAsJsonObject().get("$oid").getAsString();
                    break;
                case "PHONE_MODEL":
                    phoneModel = entry.getValue().getAsString();
                    break;
                case "ANDROID_VERSION":
                    androidVersion = entry.getValue().getAsString();
                    break;
                case "APP_VERSION_NAME":
                    appVersionName = entry.getValue().getAsString();
                    break;
                case "APP_VERSION_CODE":
                    appVersionCode = entry.getValue().getAsInt();
                    break;
                case "TOTAL_MEM_SIZE":
                    if (entry.getValue().isJsonObject()) {
                        totalMemSize = entry.getValue().getAsJsonObject().get("$numberLong").getAsLong();
                    } else {
                        totalMemSize = entry.getValue().getAsLong();
                    }
                    break;
                case "AVAILABLE_MEM_SIZE":
                    if (entry.getValue().isJsonObject()) {
                        availableMemSize = entry.getValue().getAsJsonObject().get("$numberLong").getAsLong();
                    } else {
                        availableMemSize = entry.getValue().getAsLong();
                    }
                    break;
                case "USER_APP_START_DATE":
                    startDate = entry.getValue().getAsString();
                    break;
                case "USER_CRASH_DATE":
                    crashDate = entry.getValue().getAsString();
                    break;
                case "STACK_TRACE":
                    stack = entry.getValue().getAsString();
                    stack = stack.replaceAll("\\s+at\\s+", "<br>at ");
                    break;
                case "CRASH_CONFIGURATION":
                    crashConfiguration = entry.getValue().getAsJsonObject();
                    break;
                case "SHARED_PREFERENCES":
                    sharedPreferences = entry.getValue().getAsJsonObject();
                    break;
                case "DISPLAY":
                    display = entry.getValue().getAsJsonObject();
                    break;
                case "INITIAL_CONFIGURATION":
                    initialConfiguration = entry.getValue().getAsJsonObject();
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isValid() {
        return crashDate != null && stack != null;
    }
}
