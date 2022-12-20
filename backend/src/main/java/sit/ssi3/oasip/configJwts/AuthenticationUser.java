package sit.ssi3.oasip.configJwts;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

//สำหรับการตรวจสอบสิทธิ์ หรือ authorization
//store user information
public class AuthenticationUser implements UserDetails {
    private final String username; //Returns the username used to authenticate the user .
    private final String password;
    private final Collection<Role> authorities;

    public AuthenticationUser(String username, String password, Collection<Role> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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