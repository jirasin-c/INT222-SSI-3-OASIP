package sit.ssi3.oasip.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.config.JwtTokenUtil;
import sit.ssi3.oasip.configJwts.AuthenticationUser;
import sit.ssi3.oasip.configJwts.JwtResponse;
import sit.ssi3.oasip.configJwts.JwtUserDetailsService;
import sit.ssi3.oasip.configJwts.Respond;
import sit.ssi3.oasip.dtos.LoginDTO;
import sit.ssi3.oasip.dtos.RespondUserDTO;
import sit.ssi3.oasip.entities.User;

import sit.ssi3.oasip.exceptions.ValidationLoginHandler;
import sit.ssi3.oasip.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthenticationService {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;


    public ResponseEntity match(LoginDTO oldUser) throws  Exception {
        if (userRepository.existsByEmail(oldUser.getEmail())) {
            User user = userRepository.findByEmail(oldUser.getEmail());
            if (argon2PasswordEncoder.matches(oldUser.getPassword(), user.getPassword())) {

                return ValidationLoginHandler.showError(HttpStatus.OK, "Password Matched");

            } else {
                return ValidationLoginHandler.showError(HttpStatus.UNAUTHORIZED, "Password NOT Matched");
            }
        } else {
            return ValidationLoginHandler.showError(HttpStatus.NOT_FOUND, "A user with the specified email DOES NOT exist");
        }
    }


    public ResponseEntity login(LoginDTO oldUser) throws  Exception {
        if (userRepository.existsByEmail(oldUser.getEmail())) {
            User user = userRepository.findByEmail(oldUser.getEmail());
            if (argon2PasswordEncoder.matches(oldUser.getPassword(), user.getPassword())) {
                authenticate(oldUser.getEmail() , oldUser.getPassword());

                final UserDetails userDetails = userDetailsService.loadUserByUsername(oldUser.getEmail());
                final String token = jwtTokenUtil.generateToken(userDetails);
                final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

                return ResponseEntity.ok(new JwtResponse("Login Success", token, refreshToken));

            } else {
                return ValidationLoginHandler.showError(HttpStatus.UNAUTHORIZED, "Password NOT Matched");

            }
        } else {
            return ValidationLoginHandler.showError(HttpStatus.NOT_FOUND, "A user with the specified email DOES NOT exist");

        }
    }

    public ResponseEntity refreshToken(HttpServletRequest request){
        String requestRefreshToken = request.getHeader("Authorization").substring(7);
        String userEmail = jwtTokenUtil.getUsernameFromToken(requestRefreshToken);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userEmail);
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.extractJwtFromRequest(request);

        if(jwtTokenUtil.validateToken(requestRefreshToken, userDetails)){
            return ResponseEntity.ok(new JwtResponse("Refresh Token Successfully", accessToken, refreshToken));
        }
        return Respond.respond(HttpStatus.NOT_FOUND, "Can't find Refresh Token");
    }


    private Boolean checkTokenExpired(String request){
        if(!jwtTokenUtil.isTokenExpired(request)){
            return true;
        }
        return false;
    }

    private void authenticate(String email, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
    }








}