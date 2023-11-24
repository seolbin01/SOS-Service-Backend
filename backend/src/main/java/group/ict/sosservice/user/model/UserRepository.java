package group.ict.sosservice.user.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(final Email email);

    @Query("SELECT u.child FROM User u where u.id = :id")
    Optional<User> findChildById(@Param("id") final Long id);

    boolean existsUserByEmail(final Email email);
}
