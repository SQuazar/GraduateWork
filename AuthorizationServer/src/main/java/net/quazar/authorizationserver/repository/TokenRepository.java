package net.quazar.authorizationserver.repository;

import net.quazar.authorizationserver.entity.Token;
import net.quazar.authorizationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
    List<Token> findAllByUser(User user);
    List<Token> getAllByRevokedTrue();
    void  deleteByToken(String token);
    int countAllByRevokedTrue();
    void deleteAllByRevokedTrue();
}
