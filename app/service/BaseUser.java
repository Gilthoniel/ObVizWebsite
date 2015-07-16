package service;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.user.EmailIdentity;
import com.feth.play.module.pa.user.NameIdentity;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by gaylor on 16.07.15.
 *
 */
@Entity
@Table(name = "users")
public class BaseUser extends Model implements Serializable {

    @Id
    public long id;

    @Constraints.Email
    @Column(unique = true)
    public String email;

    public String name;

    public boolean active;

    public boolean emailValidated;

    @Column(nullable=false)
    public BaseUserService.Rights right;

    @OneToMany(cascade = CascadeType.ALL)
    public List<LinkedAccount> linkedAccounts;

    /**
     * Get the linked account for the provider for the current user
     * @param providerKey key of the provider
     * @return the linked account
     */
    public LinkedAccount getAccountByProvider(final String providerKey) {

        return LinkedAccount.findByProviderKey(this, providerKey);
    }

    /**
     * Merge the other user with the current user
     * @param otherUser user to merge with
     */
    public void merge(final BaseUser otherUser) {
        for (final LinkedAccount acc : otherUser.linkedAccounts) {
            this.linkedAccounts.add(LinkedAccount.create(acc));
        }

        otherUser.active = false;
        Ebean.save(Arrays.asList(new BaseUser[]{otherUser, this}));
    }

    /**
     * Return the list of all the providers linked with this user
     * @return list of linked accounts
     */
    public Set<String> getProviders() {

        final Set<String> providerKeys = new HashSet<>(linkedAccounts.size());

        for (final LinkedAccount acc : linkedAccounts) {
            providerKeys.add(acc.providerKey);
        }

        return providerKeys;
    }

    /**
     * Finder instance
     */
    public static final Finder<Long, BaseUser> find = new Finder<>(Long.class, BaseUser.class);

    /**
     * Return true if the user exist
     * @param identity of a logged user
     * @return true if exists or false
     */
    public static boolean existsByAuthUserIdentity(final AuthUserIdentity identity) {

        final ExpressionList<BaseUser> exp = getAuthUserFind(identity);
        return exp.findRowCount() > 0;
    }

    /**
     * Find a list of base user with an identity
     * @param identity the identity
     * @return list of BaseUser
     */
    public static ExpressionList<BaseUser> getAuthUserFind(final AuthUserIdentity identity) {

        return find.where().eq("active", true)
                .eq("linkedAccounts.providerUserId", identity.getId())
                .eq("linkedAccounts.providerKey", identity.getProvider());
    }

    /**
     * Get the theorical unique user
     * @param identity identity of the user
     * @return the user
     */
    public static BaseUser findByAuthUserIdentity(final AuthUserIdentity identity) {
        if (identity == null) {
            return null;
        }

        return getAuthUserFind(identity).findUnique();
    }

    /**
     * Create a user
     * @param authUser information about the user
     * @return the created user
     */
    public static BaseUser create(final AuthUser authUser) {
        final BaseUser user = new BaseUser();
        user.active = true;
        user.linkedAccounts = Collections.singletonList(LinkedAccount.create(authUser));
        user.right = BaseUserService.Rights.USER;

        if (authUser instanceof EmailIdentity) {
            final EmailIdentity identity = (EmailIdentity) authUser;
            user.email = identity.getEmail();
            user.emailValidated = false;
        }

        if (authUser instanceof NameIdentity) {
            final NameIdentity identity = (NameIdentity) authUser;
            final String name = identity.getName();
            if (name != null) {
                user.name = name;
            }
        }

        // Because we can't access to the database easily
        if (user.email.equals("gaylor.bosson@gmail.com")) {
            user.right = BaseUserService.Rights.ADMIN;
        }

        user.save();
        return user;
    }

    /**
     * Merge two users between each other
     * @param oldUser old
     * @param newUser new
     */
    public static void merge(final AuthUser oldUser, final AuthUser newUser) {

        BaseUser.findByAuthUserIdentity(oldUser).merge(BaseUser.findByAuthUserIdentity(newUser));
    }

    /**
     * Add the linked account of the new user to the old
     * @param oldUser old
     * @param newUser new
     */
    public static void addLinkedAccount(final AuthUser oldUser, final AuthUser newUser) {

        final BaseUser user = BaseUser.findByAuthUserIdentity(oldUser);
        user.linkedAccounts.add(LinkedAccount.create(newUser));
        user.save();
    }

    /**
     * Get a user by his email
     * @param email the email
     * @return the user
     */
    public static BaseUser findByEmail(final String email) {

        return getEmailUserFind(email).findUnique();
    }

    /**
     * Get a list of potential users
     * @param email with the email
     * @return the list
     */
    private static ExpressionList<BaseUser> getEmailUserFind(final String email) {

        return find.where().eq("active", true).eq("email", email);
    }
}
