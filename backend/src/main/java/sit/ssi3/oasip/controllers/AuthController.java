package sit.ssi3.oasip.controllers;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sit.ssi3.oasip.config.JwtTokenUtil;
import sit.ssi3.oasip.configJwts.JwtResponse;
import sit.ssi3.oasip.services.AuthenticationService;
import sit.ssi3.oasip.services.LoginService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationService authenticationService;


    @GetMapping("/refreshtoken")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
        String jwtRefreshToken = extractJwtFromRequest(request);
        return authenticationService.getRefreshToken(jwtRefreshToken);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
//    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
//    public TokenRefreshResponse refreshtoken(HttpServletRequest request) throws Exception {
//        // From the HttpRequest get the claims
////        String jwtToken = extractJwtFromRequest(request);
////        Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
////        DefaultClaims claimss = (io.jsonwebtoken.impl.DefaultClaims) claims;
////
////        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claimss);
//
//        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
//
//        Map<String, Object> expectedMap = authenticationService.getMapFromIoJsonwebtokenClaims(claims);
//        String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
//
//        String requestTokenHeader = request.getHeader("Authorization");
//        return new TokenRefreshResponse(token,requestTokenHeader.substring(7));
//
//    }


//    public Map<String, Object>  getMapFromIoJsonwebtokenClaims(Claims claims){
//        Map<String, Object> expectedMap = new HashMap<String, Object>();
//        for(Map.Entry<String, Object> entry : claims.entrySet()) {
//            expectedMap.put(entry.getKey() , entry.getValue());
//        }
//        return expectedMap;
//    }


//    private String extractJwtFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7, bearerToken.length());
//        }
//        return null;
//    }
}
