package sit.ssi3.oasip.configJwts;


import org.springframework.beans.factory.annotation.Autowired;
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

    //    @Autowired
//    CustomerRepository customerRepository;
//    @Autowired
//    ReceptionistRepository receptionistRepository;
    @Autowired
    private UserRepository userRepository;

    private Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder();

    @Override
    public AuthenticationUser loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);

        if (user != null){
            Role role = new Role(user.getRole().toString());
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            return  new AuthenticationUser(user.getEmail() , argon2PasswordEncoder.encode(user.getPassword()), roles);
        }

        throw new UsernameNotFoundException("User not found with Email: " + s);
    }
}
