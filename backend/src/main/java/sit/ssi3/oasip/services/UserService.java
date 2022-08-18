package sit.ssi3.oasip.services;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;



import sit.ssi3.oasip.dtos.UserDTO;
import sit.ssi3.oasip.dtos.UserDetailDTO;
import sit.ssi3.oasip.entities.User;
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

    public UserDetailDTO getUserById(Integer userID) {
        User user = userRepository.findById(userID).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User ID " + userID + " Does not Exits"
                )
        );
        return modelMapper.map(user, UserDetailDTO.class);
    }

    public User createUser(@RequestBody UserDTO newUser)  {

          User user = modelMapper.map(newUser,User.class);
        // validate event field
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation.getMessage());
        }
        // return when error message contains
        if (violations.size() > 0) throw new ConstraintViolationException(violations);
        // custom error response
        return this.userRepository.saveAndFlush(user); // return success service
    }
















}
