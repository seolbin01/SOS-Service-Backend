package group.ict.sosservice.authentication.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RememberMeTokenRepository extends JpaRepository<RememberMeToken, Long> {

    Optional<RememberMeToken> findBySeries(final String series);

    List<RememberMeToken> findByUsername(final String username);
}
