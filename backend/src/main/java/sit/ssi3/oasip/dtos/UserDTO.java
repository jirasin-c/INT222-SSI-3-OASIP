package sit.ssi3.oasip.dtos;

import lombok.Data;
import sit.ssi3.oasip.Enum.RoleEnum;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Data
public class UserDTO implements Serializable {
    private  String name;
    private  String email;
    private  String password;
    private  String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

}
