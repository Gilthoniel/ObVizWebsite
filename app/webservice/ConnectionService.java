package webservice;

import constants.Constants;
import models.errors.BackEndRequestException;
import org.apache.http.NameValuePair;
import play.Logger;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import service.cache.CustomCache;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by gaylor on 25.06.15.
 * Method to get or post HTTP requests
 */
public class ConnectionService {

    @Inject
    private CustomCache mCache;

    /**
     * HTTP GET request
     * @param url String of the url
     * @param params list of the query parameters
     * @param type class of the object
     * @return WSResponse
     */
    public <T> F.Promise<T> get(final String url, final List<NameValuePair> params, Type type, String cacheKey)
    {

        // Populate the client object
        WSRequest client = WS.url(url).setFollowRedirects(true);
        for (NameValuePair param : params) {
            client.setQueryParameter(param.getName(), param.getValue());
        }

        // If the request is in the cache, we get it
        if (cacheKey != null && mCache.contains(cacheKey)) {

            return F.Promise.pure(mCache.get(cacheKey));
        }

        return client.get()
            .map(response -> {

                // HTTP success code is 200
                if (response.getStatus() != 200) {

                    throw new BackEndRequestException("Bad status code (" + response.getStatus() + ") for GET request [" + url + "] with queries " + params + " !");
                }

                T result = MessageParser.<T>fromJson(response.getBodyAsStream(), type);
                if (result != null) {

                    mCache.set(cacheKey, result, Constants.TIME_CACHE_EXPIRED);
                }

                return result;
            });
    }

    public <T> F.Promise<T> getNoCache(final String url, final List<NameValuePair> params, Type type) {

        return get(url, params, type, null);
    }

    /**
     * HTTP POST request
     * @param url String of the url
     * @param params list of the query parameters
     * @return WSResponse
     */
    public <T> F.Promise<T> post(final String url, final List<NameValuePair> params, Type type) {

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
    public F.Promise<Boolean> post(final String url, final List<NameValuePair> params) {
        return post(url, params, Boolean.class);
    }
}
