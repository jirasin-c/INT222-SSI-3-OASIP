package sit.ssi3.oasip.config;

import java.io.IOException;
import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.configJwts.AuthenticationUser;
import sit.ssi3.oasip.configJwts.JwtUserDetailsService;



@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            // JWT Token is in the form "Bearer token". Remove Bearer word and
            // get only the Token
            String jwtToken = extractJwtFromRequest(request);
            // if token is valid configure Spring Security to manually set
            // authentication
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            AuthenticationUser authenticationUser = this.jwtUserDetailService.loadUserByUsername(username);

            if (StringUtils.hasText(jwtToken) && jwtTokenUtil.validateToken(jwtToken , authenticationUser)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        authenticationUser, null, authenticationUser.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                System.out.println(usernamePasswordAuthenticationToken);
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                System.out.println("Cannot set the Security Context");
            }
        } catch (ExpiredJwtException ex) {
//            request.setAttribute("exception", ex);
            request.setAttribute("exception", "JWT Token has expired");
            System.out.println( ex);

//            request.setAttribute("exception", "A user with the specified email DOES NOT exist");
        } catch (BadCredentialsException ex) {
            request.setAttribute("exception", ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        chain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}