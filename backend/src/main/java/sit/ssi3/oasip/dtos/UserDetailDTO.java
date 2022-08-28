package sit.ssi3.oasip.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class UserDetailDTO implements Serializable {
    private  String name;
    private  String email;
    private  String password;
    private  String role;
    @JsonFormat(timezone = "Asia/Bangkok")
    private Timestamp createdOn;
    @JsonFormat(timezone = "Asia/Bangkok")
    private  Timestamp updatedOn;
}
