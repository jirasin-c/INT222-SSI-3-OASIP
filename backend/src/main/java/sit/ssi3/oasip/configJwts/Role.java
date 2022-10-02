package sit.ssi3.oasip.configJwts;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {
    private String authority;

    public Role(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}