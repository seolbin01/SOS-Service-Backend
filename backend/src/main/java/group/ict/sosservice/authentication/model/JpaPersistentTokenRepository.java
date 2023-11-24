package group.ict.sosservice.authentication.model;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaPersistentTokenRepository implements PersistentTokenRepository {

    private final RememberMeTokenRepository repository;

    @Override
    public void createNewToken(final PersistentRememberMeToken token) {
        repository.save(RememberMeToken.from(token));
    }

    @Override
    public void updateToken(
        final String series,
        final String tokenValue,
        final Date lastUsed
    ) {
        repository.findBySeries(series)
            .ifPresent(token -> {
                token.updateToken(tokenValue, lastUsed);
                repository.save(token);
            });
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(final String series) {
        final Optional<RememberMeToken> token = repository.findBySeries(series);

        if (token.isPresent()) {
            return mapToPersistentToken(token.get());
        }
        log.error("유효하지 않은 remember-me 토큰이 감지됨 = {}", series);
        return null;
    }

    private static PersistentRememberMeToken mapToPersistentToken(
        final RememberMeToken rememberMeToken
    ) {
        return new PersistentRememberMeToken(
            rememberMeToken.getUsername(),
            rememberMeToken.getSeries(),
            rememberMeToken.getToken(),
            rememberMeToken.getLastUsed()
        );
    }

    @Override
    public void removeUserTokens(final String username) {
        repository.deleteAllInBatch(repository.findByUsername(username));
    }
}
