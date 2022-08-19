package sit.ssi3.oasip.services;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;


import sit.ssi3.oasip.Enum.RoleEnum;
import sit.ssi3.oasip.dtos.EventcategoryDTO;
import sit.ssi3.oasip.dtos.UserDTO;
import sit.ssi3.oasip.dtos.UserDetailDTO;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.entities.User;
import sit.ssi3.oasip.repositories.UserRepository;
import sit.ssi3.oasip.utils.ListMapper;

import javax.validation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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

    public UserDetailDTO getUserByName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User ID " + name + " Does not Exits");
        return modelMapper.map(user, UserDetailDTO.class);
    }


    public User createUser(UserDTO newUser)  {

        User user = modelMapper.map(newUser,User.class);

        if(this.userRepository.findByEmail(newUser.getEmail()) != null){ user.setEmailUnique(true);};

        if(this.userRepository.findByName(newUser.getName()) != null){ user.setNameUnique(true);};

        for(RoleEnum role : RoleEnum.values()){
            if(newUser.getRole().equalsIgnoreCase(role.name()) ){
                user.setRoleSpecified(true);
            }
        }

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

}
