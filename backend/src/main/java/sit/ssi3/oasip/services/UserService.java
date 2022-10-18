package sit.ssi3.oasip.services;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.Enum.RoleEnum;
import sit.ssi3.oasip.config.JwtTokenUtil;
import sit.ssi3.oasip.dtos.RequestUserDTO;
import sit.ssi3.oasip.dtos.RespondUserDTO;
import sit.ssi3.oasip.dtos.UserDetailDTO;
import sit.ssi3.oasip.entities.Eventcategory;
import sit.ssi3.oasip.entities.User;
import sit.ssi3.oasip.exceptions.ConstraintException;
import sit.ssi3.oasip.exceptions.ValidationHandler;
import sit.ssi3.oasip.repositories.EventRepository;
import sit.ssi3.oasip.repositories.UserRepository;
import sit.ssi3.oasip.utils.ListMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private  JwtTokenUtil jwtTokenUtil;

    @Autowired
    private static final Validator validator =
            Validation.byDefaultProvider()
                    .configure()
                    .messageInterpolator(new ParameterMessageInterpolator())
                    .buildValidatorFactory()
                    .getValidator();

    private Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();




    public User getUserFromRequest(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null) {
            String token = request.getHeader("Authorization").substring(7);
            String userEmail = jwtTokenUtil.getUsernameFromToken(token);
            return  userRepository.findByEmail(userEmail);
        }
        return null;
    }

    public List<RespondUserDTO> getUser(String sortBy) {
        List<User> userList = userRepository.findAll(Sort.by(sortBy).ascending());
        if (userList.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Users");
        return listMapper.mapList(userList, RespondUserDTO.class, modelMapper);
    }

    public UserDetailDTO getUserByName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Name " + name + " Does not Exits");
        return modelMapper.map(user, UserDetailDTO.class);
    }
    public User getUserByID(Integer id) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException(id + "Does not Exist !!!"));
//        Eventcategory eventcategory = eventCategoryRepository.findById(categoryID).orElseThrow(() ->
//                new RuntimeException(categoryID + " Does not Exist !!!"));
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID" + id + " Does not Exits");
        return modelMapper.map(user, User.class);
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
//        Eventcategory eventcategory = eventCategoryRepository.findById(categoryID).orElseThrow(() ->
//                new RuntimeException(categoryID + " Does not Exist !!!"));
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email" + email + " Does not Exits");
        return modelMapper.map(user, User.class);
    }

    public Object createUser(HttpServletRequest request,RequestUserDTO newUser) {
        User userOwner = getUserFromRequest(request);

        if (userOwner != null) {
            if (userOwner.getRole().equals("student") || userOwner.getRole().equals("lecturer")) {
                return ValidationHandler.showError(HttpStatus.FORBIDDEN, "You not have permission to add new user");
            }
        }

        if (newUser.getRole().length() == 0) {
            newUser.setRole("student");
        }
        User user = modelMapper.map(newUser, User.class);
        if (!(user.getPassword().length() >= 8 && user.getPassword().length() <= 14)) {
            user.setPasswordFit(true);
        } else {
            user.setPassword(encoder.encode(newUser.getPassword()));
        }
        if (this.userRepository.findByName(newUser.getName()) != null) {
            user.setNameUnique(true);
        }
        if (this.userRepository.findByEmail(newUser.getEmail()) != null) {
            user.setEmailUnique(true);
        }
        if (RoleEnum.checkEnum(newUser.getRole())) {
            user.setRole(RoleEnum.convertToEnum(newUser.getRole()));
        } else user.setRoleSpecified(true);

        // validate event field
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation.getMessage());
        }
//        // return when error message contains
        if (violations.size() > 0) throw new ConstraintException(violations);
//         custom error response
         this.userRepository.saveAndFlush(user); // return success service
         return null;
    }

    public RequestUserDTO updateUser(RequestUserDTO updateUser, String name) {

        User newUser = userRepository.findByName(name);
        if (newUser == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Name " + name + " Does not Exits");

        if (updateUser.getName() != null) {
            User user = this.userRepository.findByName(updateUser.getName());
            if (user != null) {
                newUser.setNameUnique(!newUser.getName().equals(updateUser.getName()));
            }
            else newUser.setName(updateUser.getName());
        }

        if (updateUser.getEmail() != null) {
            User user = this.userRepository.findByEmail(updateUser.getEmail());
            if (user != null) {
                newUser.setEmailUnique(!newUser.getEmail().equals(updateUser.getEmail()));
            }
            else newUser.setEmail(updateUser.getEmail());
        }


        if (updateUser.getRole() != null) {
            if (RoleEnum.checkEnum(updateUser.getRole())) {
                newUser.setRole(RoleEnum.convertToEnum(updateUser.getRole()));
            } else newUser.setRoleSpecified(true);
        }

        // validate event field
        Set<ConstraintViolation<User>> violations = validator.validate(newUser);

        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation.getMessage());
        }
//        // return when error message contains
        if (violations.size() > 0) throw new ConstraintException(violations);

        return modelMapper.map(userRepository.saveAndFlush(newUser), RequestUserDTO.class);
    }

    public void deleteUser(String name) {
        User user = userRepository.findByName(name);
//        System.out.println(user);
//        System.out.println(name);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Name " + name + " Does not Exits");
//        eventRepository.deleteEventsByUserId(user.getId());
//        eventRepository.deleteAllByUser(user);
        userRepository.deleteUserByName(name);
    }


}
