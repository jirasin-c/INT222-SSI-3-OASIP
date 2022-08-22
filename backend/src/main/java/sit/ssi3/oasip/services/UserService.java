package sit.ssi3.oasip.services;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.Enum.RoleEnum;
import sit.ssi3.oasip.dtos.UserDTO;
import sit.ssi3.oasip.dtos.UserDetailDTO;
import sit.ssi3.oasip.entities.User;
import sit.ssi3.oasip.repositories.EventRepository;
import sit.ssi3.oasip.repositories.UserRepository;
import sit.ssi3.oasip.utils.ListMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
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
    private static final Validator validator =
            Validation.byDefaultProvider()
                    .configure()
                    .messageInterpolator(new ParameterMessageInterpolator())
                    .buildValidatorFactory()
                    .getValidator();

    public List<UserDTO> getUser(String sortBy) {
        List<User> userList = userRepository.findAll(Sort.by(sortBy).ascending());
        if (userList.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Users");
        return listMapper.mapList(userList, UserDTO.class, modelMapper);
    }

    public UserDetailDTO getUserByName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Name " + name + " Does not Exits");
        return modelMapper.map(user, UserDetailDTO.class);
    }

    public User createUser(UserDTO newUser) {

        if (newUser.getRole().length() == 0) {
            newUser.setRole("student");
        }

        User user = modelMapper.map(newUser, User.class);

        if (this.userRepository.findByEmail(newUser.getEmail()) != null) {
            user.setEmailUnique(true);
        }

        if (this.userRepository.findByName(newUser.getName()) != null) {
            user.setNameUnique(true);
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
        if (violations.size() > 0) throw new ConstraintViolationException(violations);
//         custom error response
        return this.userRepository.saveAndFlush(user); // return success service
    }

    public UserDTO updateUser(UserDTO updateUser, String name) {

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
        if (violations.size() > 0) throw new ConstraintViolationException(violations);

        return modelMapper.map(userRepository.saveAndFlush(newUser), UserDTO.class);
    }

    public void deleteUser(String name) {
        User user = userRepository.findByName(name);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Name " + name + " Does not Exits");
        eventRepository.deleteEventsByUserId(user.getId());
        userRepository.deleteUserByName(name);
    }



}
