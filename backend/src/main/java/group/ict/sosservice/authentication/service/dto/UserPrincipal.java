package group.ict.sosservice.authentication.service.dto;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import group.ict.sosservice.user.model.Role;
import lombok.Getter;

@Getter
public class UserPrincipal extends User {

    private final Long userId;

    public UserPrincipal(final group.ict.sosservice.user.model.User user) {
        super(user.getEmail().getValue(), user.getPassword(), List.of(
            new SimpleGrantedAuthority(Role.USER.getKey())
        ));
        this.userId = user.getId();
    }
}