package service;

import com.feth.play.module.pa.service.UserServicePlugin;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import play.Application;

import javax.inject.Inject;

/**
 * Created by gaylor on 16.07.15.
 * User Service for the login implementation
 */
public class BaseUserService extends UserServicePlugin {

    public enum Rights {
        USER, ADMIN;

        public static Rights getByName(String name) {
            switch (name) {
                case "admin":
                    return ADMIN;
                default:
                    return USER;
            }
        }
    }

    @Inject
    public BaseUserService(final Application app) {
        super(app);
    }

    @Override
    public Object save(final AuthUser authUser) {
        final boolean isLinked = BaseUser.existsByAuthUserIdentity(authUser);
        if (!isLinked) {
            return BaseUser.create(authUser).id;
        } else {
            // User already created
            return null;
        }
    }

    @Override
    public Object getLocalIdentity(AuthUserIdentity identity) {

        final BaseUser user = BaseUser.findByAuthUserIdentity(identity);
        if (user != null) {
            return user.id;
        } else {
            return null;
        }
    }

    @Override
    public AuthUser merge(AuthUser newUser, AuthUser oldUser) {
        if (!oldUser.equals(newUser)) {
            BaseUser.merge(oldUser, newUser);
        }

        return oldUser;
    }

    @Override
    public AuthUser link(AuthUser oldUser, AuthUser newUser) {
        BaseUser.addLinkedAccount(oldUser, newUser);
        return null;
    }
}
