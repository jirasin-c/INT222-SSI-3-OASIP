package sit.ssi3.oasip.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.config.JwtTokenUtil;
import sit.ssi3.oasip.configJwts.AuthenticationUser;
import sit.ssi3.oasip.configJwts.JwtResponse;
import sit.ssi3.oasip.configJwts.JwtUserDetailsService;
import sit.ssi3.oasip.dtos.LoginDTO;
import sit.ssi3.oasip.dtos.TokenDTO;
import sit.ssi3.oasip.entities.User;
import sit.ssi3.oasip.repositories.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtUserDetailsService jwtUserDetailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUserDetailsService userDetailsService;


    //    private Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 26, 1, 65536, 10);
    private Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    public TokenDTO match(LoginDTO oldUser) {
//        try {
        User user = userRepository.findByEmail(oldUser.getEmail());
//            System.out.println(user);
//            authenticate(oldUser.getEmail(), oldUser.getPassword())
        if (user != null) {
            final AuthenticationUser authenticationUser = jwtUserDetailService
                    .loadUserByUsername(oldUser.getEmail());
            final String token = jwtTokenUtil.generateToken(authenticationUser);
//                System.out.println("username : " + authenticationUser.getUsername() + "password : " + authenticationUser.getPassword());
            if(!(passwordEncoder.matches(oldUser.getPassword(),user.getPassword()))){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password NOT Matched");
            }
            JwtResponse<?> tokens = new JwtResponse<User>(token, user, authenticationUser.getAuthorities());
            TokenDTO tokenDto = new TokenDTO();
            tokenDto.setToken(tokens.getToken());
            return tokenDto;
        } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("Unable to generate token !"));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with the specified email DOES NOT exist");
        }

//        } catch (Exception e) {
//            new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("kuy", e.getCause()));
//        }
    }
    private void authenticate(String username, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }

    }



}
