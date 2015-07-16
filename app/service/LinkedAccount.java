package service;

import com.avaje.ebean.Model;
import com.feth.play.module.pa.user.AuthUser;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by gaylor on 16.07.15.
 *
 */
@Entity
public class LinkedAccount extends Model implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    public long id;

    @ManyToOne
    public BaseUser baseUser;

    public String providerUserId;
    public String providerKey;

    public void update(final AuthUser authUser) {
        this.providerKey = authUser.getProvider();
        this.providerUserId = authUser.getId();
    }

    public static final Finder<Long, LinkedAccount> find = new Finder<>(
            Long.class, LinkedAccount.class);

    public static LinkedAccount findByProviderKey(final BaseUser user, String key) {
        return find.where().eq("baseUser", user).eq("providerKey", key).findUnique();
    }

    public static LinkedAccount create(final AuthUser authUser) {
        final LinkedAccount ret = new LinkedAccount();
        ret.update(authUser);
        return ret;
    }

    public static LinkedAccount create(final LinkedAccount acc) {
        final LinkedAccount ret = new LinkedAccount();
        ret.providerKey = acc.providerKey;
        ret.providerUserId = acc.providerUserId;

        return ret;
    }

}
