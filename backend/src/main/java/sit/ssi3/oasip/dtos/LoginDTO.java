package sit.ssi3.oasip.dtos;

import lombok.Data;

import java.io.Serializable;


@Data
public class LoginDTO implements Serializable {
    private  String email;
    private  String password;
}
