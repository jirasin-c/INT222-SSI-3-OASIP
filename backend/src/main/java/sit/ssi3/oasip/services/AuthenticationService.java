package sit.ssi3.oasip.services;


import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.config.JwtTokenUtil;
import sit.ssi3.oasip.configJwts.AuthenticationUser;

import sit.ssi3.oasip.configJwts.JwtResponse;
import sit.ssi3.oasip.configJwts.JwtUserDetailsService;
import sit.ssi3.oasip.dtos.LoginDTO;
import sit.ssi3.oasip.entities.User;
import sit.ssi3.oasip.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    //    private Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 26, 1, 65536, 10);
    private Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();


    public ResponseEntity<?> match(LoginDTO oldUser) {
        try {
            User user = userRepository.findByEmail(oldUser.getEmail());
            if (user != null) {
                //check Password is match
                if (!(passwordEncoder.matches(oldUser.getPassword(), user.getPassword()))) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password NOT Matched");
                }
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                final AuthenticationUser authenticationUser = jwtUserDetailService.loadUserByUsername(user.getEmail());
                String jwt = jwtTokenUtil.generateToken(authenticationUser);
                List<String> roles = authenticationUser.getAuthorities().stream().map(item -> item.getAuthority())
                        .collect(Collectors.toList());
                String jwtRefreshToken = jwtTokenUtil.generateRefreshToken(authenticationUser);
                return ResponseEntity.ok(new JwtResponse(jwt, jwtRefreshToken, authenticationUser.getUsername(), roles));

            } else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with the specified email DOES NOT exist");
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

//        } catch (Exception e) {
//            new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("kuy", e.getCause()));
//        }

    private void authenticate(String username, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }

    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
//        Map<String, Object> expectedMap = new HashMap<>(claims);
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }


    public ResponseEntity<?> getRefreshToken(String jwtRefreshToken) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(jwtRefreshToken);
            AuthenticationUser authenticationUser = this.jwtUserDetailService.loadUserByUsername(username);
            String jwt = jwtTokenUtil.generateToken(authenticationUser);
            List<String> roles = authenticationUser.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt, jwtRefreshToken, authenticationUser.getUsername(), roles));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT Refresh Token has expired", ex);
        }

    }
}