package sit.ssi3.oasip.configJwts;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

    private String email;
    private String password;

    public JwtRequest() {

    }

    public JwtRequest(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }


}
