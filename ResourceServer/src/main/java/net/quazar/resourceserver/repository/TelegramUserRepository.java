package net.quazar.resourceserver.repository;

import net.quazar.resourceserver.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM telegram_users
                        JOIN subscribed_categories sc ON telegram_users.telegram_id = sc.telegram_user_id
                    WHERE sc.category_id = :categoryId
                    """)
    List<TelegramUser> findAllByCategoryId(int categoryId);

    @Query(nativeQuery = true,
            value = """
                    SELECT * from telegram_users
                        JOIN telegram_user_roles tur ON telegram_users.telegram_id = tur.telegram_user_id
                    WHERE tur.role_id = :roleId
                    """)
    List<TelegramUser> findAllByRoleId(int roleId);

    @Query(nativeQuery = true,
            value = """
                    SELECT * from telegram_users
                        JOIN telegram_user_roles tur ON telegram_users.telegram_id = tur.telegram_user_id
                        JOIN subscribed_categories sc ON telegram_users.telegram_id = sc.telegram_user_id
                    WHERE tur.role_id = :roleId AND sc.category_id = :categoryId
                    """)
    List<TelegramUser> findAllByRoleIdAndCategoryId(int roleId, int categoryId);
}
