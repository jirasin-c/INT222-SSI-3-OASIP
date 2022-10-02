package sit.ssi3.oasip.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.ssi3.oasip.dtos.LoginDTO;

import sit.ssi3.oasip.services.AuthenticationService;
import sit.ssi3.oasip.services.LoginService;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> MatchPassword(@RequestBody LoginDTO requestUser) {
        return authenticationService.match(requestUser);
    }

}
