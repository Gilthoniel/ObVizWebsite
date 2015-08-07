import com.google.inject.Guice;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.test.Helpers;
import play.test.WithApplication;
import service.cache.CustomCache;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.less.
*
*/
public class CacheTest {

    private static final String STRING_TEST = "TEST_STRING";
    private static final String KEY_CACHE = "test_key";

    @Inject
    private Application application;

    @Inject
    private CustomCache cache;

    @Before
    public void setup() {

        GuiceApplicationBuilder builder = new GuiceApplicationLoader()
                .builder(new ApplicationLoader.Context(Environment.simple()));

        Guice.createInjector(builder.applicationModule()).injectMembers(this);
    }

    @After
    public void teardown() {
        Helpers.stop(application);
    }

    @Test
    public void testAddElementInCache() {
        System.out.println("-- TEST ADD ELEMENT IN CACHE --");

        cache.set(KEY_CACHE, STRING_TEST);

        Assert.assertTrue("Cannot find entry in the cache", cache.contains(KEY_CACHE));
    }

    @Test
    public void testExpirationTime() {
        System.out.println("-- TEST EXPIRATION TIME --");

        cache.set(KEY_CACHE, STRING_TEST, 1000);
        Assert.assertNotNull("Cannot find entry in the cache", cache.get(KEY_CACHE));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertNull("Entry found in the cache but it shouldn't be the case", cache.get(KEY_CACHE));
    }

    @Test
    public void testRemoveElement() {
        System.out.println("-- TEST REMOVE ELEMENT --");

        cache.set(KEY_CACHE, STRING_TEST);
        Assert.assertNotNull("Cannot find entry in the cache", cache.get(KEY_CACHE));

        cache.remove(KEY_CACHE);
        Assert.assertNull("Entry found in the cache but it shouldn't be the case", cache.get(KEY_CACHE));
    }

    @Test
    public void testValidityObject() {
        System.out.println("-- TEST VALIDITY OBJECT --");

        List<String> list = new ArrayList<>();
        list.add("test");

        cache.set(KEY_CACHE, list);
        Assert.assertNotNull("Cannot find entry in the cache", cache.get(KEY_CACHE));

        List<String> result = cache.get(KEY_CACHE);
        Assert.assertEquals("Size of the list", result.size(), 1);
        Assert.assertEquals("Value of the field", "test", list.get(0));
    }
}
