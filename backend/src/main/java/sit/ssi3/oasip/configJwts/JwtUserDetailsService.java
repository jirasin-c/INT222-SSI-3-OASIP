package sit.ssi3.oasip.configJwts;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.ssi3.oasip.entities.User;
import sit.ssi3.oasip.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {



    private final UserRepository repository;
    private final Argon2PasswordEncoder argon2PasswordEncoder;

    public JwtUserDetailsService(UserRepository repository, Argon2PasswordEncoder argon2PasswordEncoder) {
        this.repository = repository;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
    }

    //loadUserByUsername spring จะส่ง username มาให้แล้วไปหาemail
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));

    }

    private List<GrantedAuthority> getAuthority(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        return authorities;
    }

}
