package sit.ssi3.oasip.configJwts;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import sit.ssi3.oasip.entities.User;

import java.io.Serializable;
import java.util.Collection;

public class JwtResponse<T> implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private T object;
    private Collection<? extends GrantedAuthority> role;
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    public JwtResponse(String jwttoken, T object, Collection<? extends GrantedAuthority> role) {
        this.jwttoken = jwttoken;
        if (object.getClass() == User.class) {
            String encodePassword = passwordEncoder.encode(((User) object).getPassword());
            ((User) object).setPassword(encodePassword);
            this.object = object;
        }
//       else if (object.getClass() == Receptionist.class) {
//            String encodePassword = passwordEncoder.encode(((Receptionist) object).getPassword());
//            ((Receptionist) object).setPassword(encodePassword);
//            this.object = object;
//        } else if (object.getClass() == Admin.class) {
//            String encodePassword = passwordEncoder.encode(((Admin) object).getPassword());
//            ((Admin) object).setPassword(encodePassword);
//            this.object = object;
//        }
        this.role = role;
    }

    private Argon2PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    public String getToken() {
        return this.jwttoken;
    }

    public T getAuthenticationUser() {
        return object;
    }

    public Collection<? extends GrantedAuthority> getRole() {
        return role;
    }
}