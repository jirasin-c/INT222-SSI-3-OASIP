package sit.ssi3.oasip.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class RespondUserDTO implements Serializable {
    private  String name;
    private  String email;
    private  String role;

}