package tw.com.aidenmade.rescuehero.config.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String username;
    private final String roleType;
    private final String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role); // role 內容必須是 "ROLE_ADMIN" 這種格式
    }

    @Override
    public String getPassword() {
        return "";
    }
}
