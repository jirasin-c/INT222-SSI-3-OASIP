package sit.ssi3.oasip.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.dtos.LoginDTO;
import sit.ssi3.oasip.entities.User;
import sit.ssi3.oasip.repositories.UserRepository;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;


    private Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();

    public String match(LoginDTO requestUser) {
        User user = userRepository.findByEmail(requestUser.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with the specified email DOES NOT exist.");
        }
        if (!(encoder.matches(requestUser.getPassword(),user.getPassword()))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password NOT Matched");
        }

        return "Password Matched" ;
    }
}
