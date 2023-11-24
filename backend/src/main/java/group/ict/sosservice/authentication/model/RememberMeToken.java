package group.ict.sosservice.authentication.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "persistent_logins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RememberMeToken implements Serializable {

    @Id
    private String series;

    private String username;

    private String token;

    @Column(name = "last_used")
    private Date lastUsed;

    public RememberMeToken(final PersistentRememberMeToken token) {
        this.series = token.getSeries();
        this.username = token.getUsername();
        this.token = token.getTokenValue();
        this.lastUsed = token.getDate();
    }

    public static RememberMeToken from(final PersistentRememberMeToken rememberMeToken) {
        return new RememberMeToken(rememberMeToken);
    }

    public void updateToken(final String token, final Date lastUsed) {
        this.token = token;
        this.lastUsed = lastUsed;
    }
}
