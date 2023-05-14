package net.quazar.authorizationserver.service;

import net.quazar.authorizationserver.entity.User;
import net.quazar.authorizationserver.exception.PasswordNotMatchException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    User getByUsername(@NonNull String username) throws UsernameNotFoundException;
    User save(@NonNull User user);
    User changePassword(@NonNull User user, @NonNull String newPassword) throws PasswordNotMatchException;
    void delete(@NonNull User user);
}
