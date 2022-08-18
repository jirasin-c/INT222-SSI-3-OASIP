package sit.ssi3.oasip.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class UserDTO implements Serializable {
    private  String name;
    private  String email;
    private  String role;

}
