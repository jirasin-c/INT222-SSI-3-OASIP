package sit.ssi3.oasip.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.ssi3.oasip.dtos.LoginDTO;
import sit.ssi3.oasip.services.AuthenticationService;
import sit.ssi3.oasip.services.MatchService;

@RestController
@RequestMapping("/api/match")
public class MatchController  {

    @Autowired
    private MatchService matchService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public String MatchPassword(@RequestBody LoginDTO requestUser) {
        return matchService.match(requestUser);
    }

}