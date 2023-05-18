package net.quazar.resourceserver.repository;

import net.quazar.resourceserver.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
    List<Token> findAllByUserId(int userId);
    List<Token> getAllByRevokedTrue();
    void  deleteByToken(String token);
    @Query(value = "delete from Token where token in :tokens")
    void deleteAllByToken(List<String> tokens);
    int countAllByRevokedTrue();
    void deleteAllByRevokedTrue();
}
