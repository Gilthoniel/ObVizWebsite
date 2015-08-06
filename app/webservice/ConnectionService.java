package webservice;

import constants.Constants;
import models.errors.ServerOverloadedException;
import org.apache.http.NameValuePair;
import play.Logger;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by gaylor on 25.06.15.
 * Method to get or post HTTP requests
 */
public class ConnectionService {

    /**
     * HTTP GET request
     * @param url String of the url
     * @param params list of the query parameters
     * @param type class of the object
     * @param networkException exception thrown if a network error occurred
     * @param nullException exception thrown if a null result occurred
     * @return WSResponse
     */
    public static <T> F.Promise<T> get(final String url, final List<NameValuePair> params, Type type, String cacheKey,
                                       Throwable networkException, Throwable nullException)
    {

        // Populate the client object
        WSRequest client = WS.url(url).setFollowRedirects(true);
        for (NameValuePair param : params) {
            client.setQueryParameter(param.getName(), param.getValue());
        }

        // If the request is in the cache, we get it
        if (cacheKey != null && CustomCache.contains(cacheKey)) {
            return F.Promise.pure(CustomCache.<T>take(cacheKey));
        }

        return client.get()
            .map(response -> {

                // HTTP success code is 200
                if (response.getStatus() != 200) {

                    Logger.warn("Bad status code (" + response.getStatus() + ") for GET request [" + url + "] with queries " + params + " !");
                    throw networkException;
                }

                T result = MessageParser.<T>fromJson(response.getBodyAsStream(), type);
                if (result != null) {

                    CustomCache.put(cacheKey, result);

                    return result;
                }

                throw nullException;
            });
    }

    public static <T> F.Promise<T> get(final String url, final List<NameValuePair> params, Type type, String cacheKey) {

        Throwable error = new ServerOverloadedException();

        return ConnectionService.<T>get(url, params, type, cacheKey, error, error);
    }

    public static <T> F.Promise<T> getNoCache(final String url, final List<NameValuePair> params, Type type) {
        Throwable error = new ServerOverloadedException();

        return ConnectionService.<T>get(url, params, type, null, error, error);
    }

    /**
     * HTTP POST request
     * @param url String of the url
     * @param params list of the query parameters
     * @return WSResponse
     */
    public static <T> F.Promise<T> post(final String url, final List<NameValuePair> params, Type type) {

        // Populate the client
        StringBuilder uri = new StringBuilder();
        for(NameValuePair value : params) {
            uri.append(value.getName()).append("=").append(value.getValue()).append("&");
        }

        WSRequest client = WS.url(url)
            .setContentType("application/x-www-form-urlencoded")
            .setMethod("POST")
            .setBody(uri.toString());

        return client.execute()
            .map(response -> {

                if (response.getStatus() != 200) {
                    Logger.warn("Bad status code (" + response.getStatus() + ") for POST request [" + url + "] with queries " + params + " !");
                }

                return MessageParser.<T>fromJson(response.getBody(), type);
            })
            .recover(throwable -> {

                Logger.error("POST connection error : " + url + " with queries : " + params.toString());
                return null;
            });
    }

    /**
     * POST request with a boolean as result
     * @param url URL of the request
     * @param params query parameters of the request
     * @return true if success
     */
    public static F.Promise<Boolean> post(final String url, final List<NameValuePair> params) {
        return post(url, params, Boolean.class);
    }
}
