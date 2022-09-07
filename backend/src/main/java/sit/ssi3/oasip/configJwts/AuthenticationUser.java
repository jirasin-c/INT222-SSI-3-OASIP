package sit.ssi3.oasip.configJwts;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sit.ssi3.oasip.Enum.RoleEnum;

import java.util.Collection;

public class AuthenticationUser implements UserDetails {
    private final String username;
    private final String password;
    private final RoleEnum authorities;

    public AuthenticationUser(String username, String password, RoleEnum authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.authorities;
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}