package sit.ssi3.oasip.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDetailDTO implements Serializable {
    private  String name;
    private  String email;
    private  String role;
    private  Date createdOn;
    private  Date updatedOn;
}
