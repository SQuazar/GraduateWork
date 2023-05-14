package net.quazar.authorizationserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.quazar.authorizationserver.entity.User;
import net.quazar.authorizationserver.exception.PasswordNotMatchException;
import net.quazar.authorizationserver.repository.UserRepository;
import net.quazar.authorizationserver.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getByUsername(@NonNull String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Пользователь с именем '%s' не найден.".formatted(username))
        );
    }

    @Transactional
    @Override
    public User save(@NonNull User user) {
        return repository.saveAndFlush(user);
    }

    @Override
    public User changePassword(@NonNull User user, @NonNull String newPassword) throws PasswordNotMatchException {
        final String password;
        if (!user.getPassword().equals(password = passwordEncoder.encode(newPassword)))
            throw new PasswordNotMatchException("Пароли не совпадают!");
        user.setPasswordHash(password);
        return repository.save(user);
    }

    @Override
    public void delete(@NonNull User user) {
        repository.delete(user);
    }
}
