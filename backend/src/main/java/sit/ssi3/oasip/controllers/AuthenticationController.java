package sit.ssi3.oasip.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.ssi3.oasip.dtos.LoginDTO;
import sit.ssi3.oasip.services.AuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity Login(@Valid @RequestBody LoginDTO requestUser) throws Exception {
        return authenticationService.login(requestUser);
    }

    @PostMapping("/match")
    public ResponseEntity Match(@Valid @RequestBody LoginDTO requestUser) throws Exception {
        return authenticationService.match(requestUser);
    }

    @GetMapping("/refresh")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity refreshToken(@Valid HttpServletRequest request){
        return authenticationService.refreshToken(request);
    }

}
