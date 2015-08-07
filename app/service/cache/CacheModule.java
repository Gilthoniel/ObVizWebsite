package service.cache;

import com.google.inject.AbstractModule;
import play.api.Configuration;
import play.api.Environment;
import play.cache.CacheApi;

/**
 * Created by gaylor on 06-Aug-15.
 *
 */
public class CacheModule extends AbstractModule {

    private Environment environment;
    private Configuration configuration;

    public CacheModule(Environment env, Configuration conf) {
        environment = env;
        configuration = conf;
    }


    @Override
    protected void configure() {

        bind(CacheApi.class).to(CustomCache.class);
    }
}
