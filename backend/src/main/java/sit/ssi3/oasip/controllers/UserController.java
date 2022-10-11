package sit.ssi3.oasip.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.ssi3.oasip.dtos.RequestUserDTO;
import sit.ssi3.oasip.dtos.RespondUserDTO;
import sit.ssi3.oasip.dtos.UserDetailDTO;
import sit.ssi3.oasip.entities.User;
import sit.ssi3.oasip.services.UserService;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<RespondUserDTO> getUser(@RequestParam(defaultValue = "name") String sortBy){
        return userService.getUser(sortBy);
    }

    @GetMapping("/{name}")
    public UserDetailDTO  getUserByName(@PathVariable String name){
        return userService.getUserByName(name);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity createUser(@RequestBody RequestUserDTO newUser) {
        userService.createUser(newUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{name}")
    public RequestUserDTO updateUser(@RequestBody RequestUserDTO updateUser, @PathVariable String name){
        return  userService.updateUser(updateUser,name);
    }

    @DeleteMapping("/{name}")
    public void deleteUser(@PathVariable String name){
        userService.deleteUser(name);
    }


}
