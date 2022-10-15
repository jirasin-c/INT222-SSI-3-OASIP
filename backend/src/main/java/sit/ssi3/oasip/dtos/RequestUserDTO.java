package sit.ssi3.oasip.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sit.ssi3.oasip.Enum.RoleEnum;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Data
public class RequestUserDTO implements Serializable {
    private  String name;
    private  String email;
    private  String password;
    private  String role;

}
