package sit.ssi3.oasip.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.ssi3.oasip.dtos.LoginDTO;
import sit.ssi3.oasip.services.AuthenticationService;
import sit.ssi3.oasip.services.RefreshService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class AuthController {

    private RefreshService refreshService;

    @Autowired
    public AuthController(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/refresh")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity refreshLogin(@Valid HttpServletRequest request){
        return refreshService.refreshToken(request);
    }

    @PostMapping("/login")
    public ResponseEntity Login(@Valid @RequestBody LoginDTO requestUser) throws Exception {
        return authenticationService.login(requestUser);
    }

    @PostMapping("/match")
    public ResponseEntity Match(@Valid @RequestBody LoginDTO requestUser) throws Exception {
        return authenticationService.match(requestUser);
    }

}
