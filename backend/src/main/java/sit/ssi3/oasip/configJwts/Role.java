package sit.ssi3.oasip.configJwts;

import org.springframework.security.core.GrantedAuthority;

//class ที่ใช้สำหรับระบุสิทธิ์ การเข้าถึง  ซึ่งในที่นี้เราจะระบุกี่สิทธิ์ก็ได้
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