package service.cache;

import net.sf.ehcache.Element;

/**
 * Created by gaylor on 08/07/2015.
 * Extend the ehcache element to add an expiration delay
 */
public class ExpirableElement extends Element {

    private static final long serialVersionUID = -3575469568232971776L;
    private final long mExpirationTime;

    /**
     * Constructor
     * @param key Key of the element
     * @param value Value of the element
     * @param expirationTime Expiration time in millisecond from the current Timestamp
     */
    public ExpirableElement(final String key, final Object value, int expirationTime) {
        super(key, value);

        mExpirationTime = System.currentTimeMillis() + expirationTime;
    }

    @Override
    public boolean isExpired() {

        return System.currentTimeMillis() > mExpirationTime;
    }
}
